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


public class HisenseMQSocket implements SocketInterfacec {
	
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
	
	private enum recordType {
		SG_PASSING,  // 交通过车数据
		SG_ILLEGAL,  // 交通违法数据
		SG_FLOW,     // 交通流量数据
		SG_EVENT,    // 交通事件数据
	};
	
	private recordType messageType;
	
	// 解析MQ消息
	private ParseHisenseMQ parseMq;
	
	public static final String HISENSE_PASSING = "HIATMP.HISENSE.PASS.PASSINF";
	public static final String HISENSE_ILLEGAL = "HIATMP.HISENSE.ILLEGAL";
	
	public static final String REMOTE_URL1 = "tcp://52.1.101.193:61616";
	public static final String REMOTE_URL2 = "tcp://52.1.101.195:61616";
	public static final String REMOTE_URL3 = "tcp://10.161.14.6:61616";
	
	public HisenseMQSocket( String remoteURL, String remoteTopic, int milliseconds )
	{
		this.remoteTopic = remoteTopic;
		this.remoteURL   = remoteURL;
		this.milliseconds= milliseconds;
		
		parseMq = new ParseHisenseMQ();
		
		// 确认消息内容，默认消息为过车数据
		if ( "HIATMP.HISENSE.ILLEGAL".equals(this.remoteTopic) )
			messageType = recordType.SG_ILLEGAL;
		else if ( "HIATMP.HISENSE.PASS.PASSINF".equals(this.remoteTopic) )
			messageType = recordType.SG_PASSING;
		else 
			messageType = recordType.SG_PASSING;
		
		// 确认有效时间，默认连接时间为15s
		if ( this.milliseconds < 0 ) this.milliseconds = 1000 * 15;
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

	public byte[] recv(String code) throws JMSException, UnknownStringException, UnsupportedEncodingException 
	{
		Message msg = consumer.receive(milliseconds);
		TextMessage textMessage = (TextMessage)msg;
		
		String xml = "";
		
		if ( messageType == recordType.SG_PASSING )
		{
			xml = parseMq.convertPassingInfo( textMessage.getText() );
		}
		if ( messageType == recordType.SG_ILLEGAL )
		{
			xml = parseMq.convertIllegalInfo( textMessage.getText() );
		}
		
		// 若用户指定的编码格式不用，那么采用默认的UTF-8消息格式
		return xml.getBytes( code );
	}

	public byte[] recv() throws JMSException, UnknownStringException 
	{
		Message msg = consumer.receive(milliseconds);
		TextMessage textMessage = (TextMessage)msg;
		
		String xml = "";
		
		if ( messageType == recordType.SG_PASSING )
		{
			xml = parseMq.convertPassingInfo( textMessage.getText() );
		}
		if ( messageType == recordType.SG_ILLEGAL )
		{
			xml = parseMq.convertIllegalInfo( textMessage.getText() );
		}
		
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
