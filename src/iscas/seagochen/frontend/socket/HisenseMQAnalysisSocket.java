package iscas.seagochen.frontend.socket;

import iscas.seagochen.exceptions.UnimplementedMethodException;
import iscas.seagochen.exceptions.UnknownStringException;
import iscas.seagochen.frontend.parse.ParseHisenseMQ;

import java.io.UnsupportedEncodingException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;


public class HisenseMQAnalysisSocket implements SocketInterfacec {
	
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
	
	// MQ ���ӵ�ַ
	private String remoteURL;
	// MQ topic
	private String remoteTopic;
	// Milliseconds
	private int milliseconds;
	
	public static final String HISENSE_PASSING = "HIATMP.HISENSE.PASS.PASSINF";
	public static final String HISENSE_ILLEGAL = "HIATMP.HISENSE.ILLEGAL";
	
	public static final String REMOTE_URL1 = "tcp://52.1.101.193:61616";
	public static final String REMOTE_URL2 = "tcp://52.1.101.195:61616";
	public static final String REMOTE_URL3 = "tcp://10.161.14.6:61616";
	
	public HisenseMQAnalysisSocket( String remoteURL, String remoteTopic, int milliseconds )
	{
		this.remoteTopic = remoteTopic;
		this.remoteURL   = remoteURL;
		this.milliseconds= milliseconds;
		
		new ParseHisenseMQ();
		
		// ȷ����Ϣ���ݣ�Ĭ����ϢΪ��������
		if ( "HIATMP.HISENSE.ILLEGAL".equals(this.remoteTopic) ) {
		} else if ( "HIATMP.HISENSE.PASS.PASSINF".equals(this.remoteTopic) ) {
		} else {
		}
		
		// ȷ����Чʱ�䣬Ĭ������ʱ��Ϊ15s
		if ( this.milliseconds < 0 ) this.milliseconds = 1000 * 15;
	}

	public void conf() throws JMSException 
	{
		// �����µ�����
		connFactory = new ActiveMQConnectionFactory(remoteURL);	
		// ��ȡ���Ӷ���
		connection = connFactory.createConnection();
	}

	public void connect() throws JMSException 
	{
		// ����
		connection.start();
		// create a session
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		// select a topic
		topic = session.createTopic(remoteTopic);
		// create a message consumer from the session to the topic
		consumer = session.createConsumer(topic);
	}

	public byte[] recv(String code) throws JMSException, UnsupportedEncodingException 
	{
		Message msg = consumer.receive(milliseconds);
		TextMessage textMessage = (TextMessage)msg;
		
		String xml = textMessage.getText();
		
		// ���û�ָ���ı����ʽ���ã���ô����Ĭ�ϵ�UTF-8��Ϣ��ʽ
		return xml.getBytes( code );
	}

	public byte[] recv() throws JMSException 
	{
		Message msg = consumer.receive(milliseconds);
		TextMessage textMessage = (TextMessage)msg;
		
		String xml = textMessage.getText();
		
		return xml.getBytes();
	}

	public void send(byte[] pack) throws UnimplementedMethodException
	{
		throw new UnimplementedMethodException("method send not implemented");
	}

	public void close() throws JMSException
	{
		consumer.close();
		session.close();
	}

}
