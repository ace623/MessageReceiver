import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConsumer {
	public static void main( String args[] ) throws Exception
	{
		// ConnectionFactory: 连接工厂，创建连接
		ConnectionFactory connFactory;
		// Connection: JMS客户端到JMS Provider的连接
		Connection connection;
		// Session: 接收消息的线程
		Session session;
		// Destination: 消息目的地
		Destination destination;
		// Consumer：消费者，接收消息
		MessageConsumer consumer;
		// Topic： 主题消息类型
		Topic topic;
		
		// 用户名、密码及设定连接地址
		String name = null;
		String passwd = null;
		String url = "tcp://52.1.101.195:61616";
		// 创建新的连接
		connFactory = new ActiveMQConnectionFactory(url);
		//connFactory = new ActiveMQConnectionFactory(name, passwd, url);
		
		try {
			// 获取连接对象
			connection = connFactory.createConnection();
			// 启动
			connection.start();
			// create a session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// select a topic
			topic = session.createTopic("HIATMP.HISENSE.PASS.PASSINF");
			// create a message consumer from the session to the topic
			consumer = session.createConsumer(topic);
			
			Message message = consumer.receive(10000);
			
			if ( message instanceof TextMessage ) {
				TextMessage textMessage = (TextMessage) message;
				String text = textMessage.getText();
				System.out.println( text );
			}
			else {
				System.out.println( message );
			}

/*			consumer.setMessageListener( new MessageListener() {
				public void onMessage(Message message) {
					TextMessage tm = (TextMessage) message;
					try { 
						System.out.println("Received message: " + tm.getText()); 
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			} );
*/			
			consumer.close();
			session.close();
			connection.close();
			
		} catch ( Exception e ) {
			e.printStackTrace();
		} 
		
		System.out.println( "exit..." );
	}
}
