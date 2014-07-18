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
	// ConnectionFactory: 连接工厂，创建连接
	private static ConnectionFactory connFactory;
	// Connection: JMS客户端到JMS Provider的连接
	private static Connection connection;
	// Session: 接收消息的线程
	private static Session session;
	// Consumer：消费者，接收消息
	private static MessageConsumer consumer;
	// Topic： 主题消息类型
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
			// 创建新的连接
			connFactory = new ActiveMQConnectionFactory(url);	
			// 获取连接对象
			connection = connFactory.createConnection();
			// 启动
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
