package iscas.seagochen.frontend.socket;

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

import iscas.seagochen.frontend.FrontEndSocket;

public class HisenseMQSocket extends FrontEndSocket {
	
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
	
	public HisenseMQSocket( String remoteURL, String remoteTopic, int milliseconds )
	{
		this.remoteTopic = remoteTopic;
		this.remoteURL   = remoteURL;
		this.milliseconds= milliseconds;
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
		return textMessage.getText().getBytes(code);
	}

	public byte[] recv() throws JMSException, UnsupportedEncodingException 
	{
		return recv("UTF-8");
	}

	public void send(byte[] pack) throws Exception
	{
		//TODO
	}

	public void close() throws JMSException
	{
		consumer.close();
		session.close();
	}

}
