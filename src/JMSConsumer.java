import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
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
	// ConnectionFactory: ���ӹ�������������
	public static ConnectionFactory connFactory;
	// Connection: JMS�ͻ��˵�JMS Provider������
	public static Connection connection;
	// Session: ������Ϣ���߳�
	public static Session session;
	// Consumer�������ߣ�������Ϣ
	public static MessageConsumer consumer;
	// Topic�� ������Ϣ����
	public static Topic topic; 
	// fixed URL
	private static String url = "tcp://52.1.101.195:61616";
	// fixed topic
	private static String myTopic = "HIATMP.HISENSE.PASS.PASSINF";
	//private static String myTopic = "HIATMP.HISENSE.ILLEGAL";
	// �߳���
	private static ReentrantLock lock;
	// flag
	private static boolean flag;
	// Converter
	private static ConvertHisenseMQ    convertMQ;
	private static ProduceISCASPackage iscasPack;
	// counter
	private static int count;
	// socket
	private static Socket socket;
	// socket I/O
	private static DataOutputStream output;
	private static DataInputStream  input;
	// TCP tokens
	private static byte[] tokens;
	// time
	private static Timer timer;
	
	private static void init()
	{
		timer = new Timer();
		convertMQ = new ConvertHisenseMQ();
		iscasPack = new ProduceISCASPackage();
	}
	
	private static void connect()
	{
		final String serverAddr = "52.1.126.70";
		final int    serverPort = 12351;
		
		try {
			socket = new Socket( serverAddr, serverPort );
//			socket.setSoTimeout(5000); // set time out as 5s
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		} catch ( Exception e ) {
			e.printStackTrace();
		}	
	}
	
	private static void setApacheMQ()
	{
		// �����߳���
		lock = new ReentrantLock();
		count = 0;
		
		// ���Ķ���
		ApacheMQConnector connector;
		// ���߳�
		Thread thread;
		
		// �������Ķ���
		connector = new ApacheMQConnector();
		connector.CreateConnection(url, myTopic);
		
		// �����������ӽ���
		flag = true;
		thread = new Thread(new JMSConsumer());
		thread.setDaemon(true);
		thread.start();
		
		// ��������
		Scanner scanner = new Scanner(System.in);
		String command = null;
		
		while ( !"exit".equals(command) )
		{
			command = scanner.nextLine();
		}
		
		// ֹͣ��������
		flag = false;
		
		// �ر�һ������
		connector.CloseConnection();
		
		scanner.close();
		System.out.println("the number of records: " + count);
		System.out.println("exit JMS listener...");
		System.exit(0);		
	}

	private static void genTCPTokens( String MQ )
	{
		tokens = iscasPack.produceISCASPackage(0,
				convertMQ.convertPassingInfo(MQ));
	}
	
	private boolean SendMQToServer( String MQ )
	{
		timer.resetTimer();
		
		connect();
		genTCPTokens( MQ );
		timer.printCurrent( "connection" );
		
		if ( !socket.isConnected() )
		{
			System.out.println( "connection failed!" );
			return false;
		}
			
		try {
			output.write(tokens);
			output.flush();
			System.out.println( "write success" );
			timer.printCurrent( "send" );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
//		try {
//			byte[] buf = new byte[128];
//			timer.printCurrent( "receive 0" );
//			input.read(buf); //�����������ط�2s
//			timer.printCurrent( "receive 1" );
//			System.out.println( "success > " + new String(buf) );
//			timer.printCurrent( "receive 2" );
//		} catch ( IOException e )
//		{
//			e.printStackTrace();
//		}
		
		try {			
			output.close();
			input.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		timer.printCurrent( "close connection" );
		return true;
	}

	public static void main( String args[] )
	{
		init();
		setApacheMQ();	
	}
	
	public void run()
	{
		while (flag)
		{
			lock.lock();
			
			try 
			{
				Message message = consumer.receive(10000);
				
				if ( message instanceof TextMessage )
				{
					TextMessage textMessage = (TextMessage) message;
					String text = textMessage.getText();
//					System.out.println( text );
					SendMQToServer( text );
					
					count++;
				}				
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
			
			lock.unlock();
		}
	}

}

class ApacheMQConnector {
	
	public void CloseConnection()
	{
		try
		{ 
			JMSConsumer.consumer.close();
			JMSConsumer.session.close();
			JMSConsumer.connection.close(); 
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void CreateConnection( String url, String topic )
	{
		try
		{
			// �����µ�����
			JMSConsumer.connFactory = new ActiveMQConnectionFactory(url);	
			// ��ȡ���Ӷ���
			JMSConsumer.connection = JMSConsumer.connFactory.createConnection();
			// ����
			JMSConsumer.connection.start();
			// create a session
			JMSConsumer.session = JMSConsumer.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// select a topic
			JMSConsumer.topic = JMSConsumer.session.createTopic(topic);
			// create a message consumer from the session to the topic
			JMSConsumer.consumer = JMSConsumer.session.createConsumer(JMSConsumer.topic);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}	
