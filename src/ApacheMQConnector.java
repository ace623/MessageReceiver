import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;


public class ApacheMQConnector {
	// ConnectionFactory: ���ӹ�������������
	private static ConnectionFactory connFactory;
	// Connection: JMS�ͻ��˵�JMS Provider������
	private static Connection connection;
	// Session: ������Ϣ���߳�
	private static Session session;
	// Consumer�������ߣ�������Ϣ
	private static MessageConsumer consumer;
	// Topic�� ������Ϣ����
	private static Topic topic; 
	
	public void close()
	{
		try
		{ 
			consumer.close();
			session.close();
			connection.close(); 
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void connect( String url, String myTopic )
	{
		try
		{
			// �����µ�����
			connFactory = new ActiveMQConnectionFactory(url);	
			// ��ȡ���Ӷ���
			connection = connFactory.createConnection();
			// ����
			connection.start();
			// create a session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// select a topic
			topic = session.createTopic(myTopic);
			// create a message consumer from the session to the topic
			consumer = session.createConsumer(topic);
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	public String recv( int millseconds ) throws JMSException
	{
		Message msg = consumer.receive(millseconds);
		
		if ( msg instanceof TextMessage )
		{
			TextMessage textMessage = (TextMessage)msg;
			 return textMessage.getText();
		}
		
		return "";
	}
}	
