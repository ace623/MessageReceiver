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
import seagosoft.iscas.socket.*; 

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
	// Convertor
//	PackingHisenseMQMessage packMsg;
	
	public static void main( String args[] )
	{
		// �����߳���
		lock = new ReentrantLock();
		
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
		System.out.println("exit JMS listener...");
		System.exit(0);
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
					System.out.println( text );
				}
				else
				{
					System.out.println( message );
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
