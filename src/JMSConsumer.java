import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSConsumer {
	public static void main( String args[] ) throws Exception
	{
		// ConnectionFactory: ���ӹ�������������
		ConnectionFactory connFactory;
		// Connection: JMS�ͻ��˵�JMS Provider������
		Connection connection;
		// Session: ������Ϣ���߳�
		Session session;
		// Destination: ��ϢĿ�ĵ�
		Destination destination;
		// Consumer�������ߣ�������Ϣ
		MessageConsumer consumer;
		// Topic�� ������Ϣ����
		Topic topic;
		
		// �û��������뼰�趨���ӵ�ַ
		String name = null;
		String passwd = null;
		String url = "tcp://52.1.101.195:61616";
		// �����µ�����
		connFactory = new ActiveMQConnectionFactory(url);
		//connFactory = new ActiveMQConnectionFactory(name, passwd, url);
		
		try {
			// ��ȡ���Ӷ���
			connection = connFactory.createConnection();
			// ����
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
