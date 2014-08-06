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
	
	// MQ 连接地址
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
		// 创建新的连接
		connFactory = new ActiveMQConnectionFactory(remoteURL);	
		// 获取连接对象
		connection = connFactory.createConnection();
	}

	public void connect() throws JMSException 
	{
		// 启动
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
