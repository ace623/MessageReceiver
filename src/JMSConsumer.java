import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import seagosoft.iscas.socket.ConvertHisenseMQ;
import seagosoft.iscas.socket.ProduceISCASPackage;
import seagosoft.iscas.tools.*;

public class JMSConsumer implements Runnable
{
	// fixed URL
	private static String url1 = "tcp://52.1.101.195:61616";
	private static String url2 = "tcp://52.1.101.193:61616"; 
	private static String url3 = "tcp://10.161.14.6:61616";
	
	// fixed topic
	private static String passingTopic = "HIATMP.HISENSE.PASS.PASSINF";
	private static String illegalTopic = "HIATMP.HISENSE.ILLEGAL";
	
	// 线程锁
	private static ReentrantLock lock;
	
	// flag
	private static boolean flag;
	
	// 自定义数据类型
	private static ApacheMQConnector   connector; // MQ 连接助手
	private static ConvertHisenseMQ    convertMQ; // 消息转换助手
	private static ProduceISCASPackage producer;  // 数据包转换助手
	private static ISCASSocketClient   client;    // 网络通信助手
	private static Timer               timer;     // 计时器
	
	// 计数器
	private static int count_mq;
	private static int count_lost;
	private static int count_send;
	
	private static boolean cmd()
	{
		Thread thread = new Thread(new JMSConsumer());
		
		thread.start();
		
		// 监听输入
		Scanner scanner = new Scanner(System.in);
		String command = null;
		
		while ( !"exit".equals(command) )
		{
			command = scanner.nextLine();
		}
		
		scanner.close();
		return true;
	}
	
	private static void init( String apacheTopic )
	{
		connector = new ApacheMQConnector();
		convertMQ = new ConvertHisenseMQ();
		producer  = new ProduceISCASPackage();
		client    = new ISCASSocketClient();
		timer     = new Timer();
		lock      = new ReentrantLock();
		
		client.SetSocketClient(convertMQ, producer);		
		connector.connect(url1, apacheTopic);	
		count_mq = count_lost = count_send = 0;
		flag = true;
	}
	
	public static void main( String[] argv )
	{
		if ( "passing".equals(argv[0]) ) 
			init( passingTopic );
		if ( "illegal".equals(argv[0]) )
			init( illegalTopic );		
		
		if ( cmd() )
		{
			flag = false;
			connector.close();
			
			System.out.println( "pull from mq: " + count_mq );
			System.out.println( "lost package: " + count_lost );
			System.out.println( "package sent: " + count_send );
			System.out.println( "goodbye!" );
		}
	}

	
	public void run()
	{
		count_mq = count_lost = count_send = 0;
		
		while (flag)
		{
			lock.lock();
			
			try 
			{
				// 从MQ接收数据
				String str = connector.recv(10000);
				count_mq++;
				
				// 连接后端服务器
				client.connect( "52.1.126.70", 12350 );
				
				// 发送数据
				if ( !client.send(str, 0, ISCASSocketClient.SG_GENERATE_PASSING_TOKENS) )
				{
					System.out.println( "send failed!" );
					count_lost++;
				}

				// 发送成功
				count_send++;
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			lock.unlock();
		}
	}

}