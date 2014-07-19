import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;

import seagosoft.iscas.socket.ConvertHisenseMQ;
import seagosoft.iscas.socket.ProduceISCASPackage;


public class ISCASSocketClient {

	private Socket socket;
	
	private DataOutputStream output;
	private DataInputStream  input;
	
	private byte[] sendTokens;
	private byte[] recvTokens;
	
	private ConvertHisenseMQ    converter;
	private ProduceISCASPackage producer;
	
	public static final int SG_GENERATE_PASSING_TOKENS = 0;
	public static final int SG_GENERATE_ILLEGAL_TOKENS = 1;
	
	public ISCASSocketClient()
	{
		converter = new ConvertHisenseMQ();
		producer  = new ProduceISCASPackage();
	}
	
	/**
	 * 创建与服务器的TCP连接链路
	 * @param servAddr    服务器ipv4地址
	 * @param port        端口号
	 * @param millseconds 超时连接，单位毫秒
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public void connect( String servAddr, int port, int millseconds ) throws UnknownHostException, IOException
	{
		socket = new Socket( servAddr, port );
		socket.setSoTimeout(millseconds);
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());	
	}
	
	/**
	 * 创建与服务器的TCP连接链路
	 * @param servAddr    服务器ipv4地址
	 * @param port        端口号
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */	
	public void connect( String servAddr, int port ) throws UnknownHostException, IOException
	{
		socket = new Socket( servAddr, port );
		input = new DataInputStream(socket.getInputStream());
		output = new DataOutputStream(socket.getOutputStream());
	}
	
	/**
	 * 将MQ消息转换之后发送给服务器，默认编码格式为UTF-8
	 * @param mq     来自MQ的消息
	 * @param type   数据类型，过车数据(0)，闯红灯数据(1)
	 * @throws IOException 
	 */
	public boolean send( String mq, int times, int type ) throws IOException
	{
		String data;
		switch( type )
		{
		case SG_GENERATE_PASSING_TOKENS:
			data = converter.convertPassingInfo(mq);
			sendTokens = producer.produceISCASPackage(times, data);
			break;
			
		case SG_GENERATE_ILLEGAL_TOKENS:
			data = converter.convertIllegalInfo(mq);
			sendTokens = producer.produceISCASPackage(times, data);
			break;
			
			default: break;
		}
		
		output.write(sendTokens);
		output.flush();
		
		return true;
	}

	/**
	 * 发送文本数据，默认编码格式为UTF-8
	 * @param context
	 * @throws UnsupportedEncodingException, IOException 
	 */
	public void sendMSG( String context ) throws  UnsupportedEncodingException, IOException
	{
		output.write( context.getBytes("UTF-8") );
		output.flush();
	}
	
	/**
	 * 发送文本数据，编码格式由用户指定
	 * @param context
	 * @param encoding
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void sendMSG( String context, String encoding ) throws UnsupportedEncodingException, IOException
	{
		output.write( context.getBytes(encoding) );
		output.flush();
	}
	
	/**
	 * 接收远程服务器返回的信息，默认编码格式为UTF-8
	 * @return  String类型的消息数据
	 * @throws IOException 
	 * @throws Exception
	 */
	public String recv() throws IOException
	{
		String str = "";

		int recv = input.read(recvTokens);
		
		if ( recv < 0 )
		{
			System.out.println( "received failed" );
			return str;
		}
		
		str = new String( recvTokens, "UTF-8" );
		
		return str;
	}
	
	/**
	 * 关闭数据连接
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		input.close();
		output.close();
		socket.close();
	}
}
