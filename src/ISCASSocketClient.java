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
	 * �������������TCP������·
	 * @param servAddr    ������ipv4��ַ
	 * @param port        �˿ں�
	 * @param millseconds ��ʱ���ӣ���λ����
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
	 * �������������TCP������·
	 * @param servAddr    ������ipv4��ַ
	 * @param port        �˿ں�
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
	 * ��MQ��Ϣת��֮���͸���������Ĭ�ϱ����ʽΪUTF-8
	 * @param mq     ����MQ����Ϣ
	 * @param type   �������ͣ���������(0)�����������(1)
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
	 * �����ı����ݣ�Ĭ�ϱ����ʽΪUTF-8
	 * @param context
	 * @throws UnsupportedEncodingException, IOException 
	 */
	public void sendMSG( String context ) throws  UnsupportedEncodingException, IOException
	{
		output.write( context.getBytes("UTF-8") );
		output.flush();
	}
	
	/**
	 * �����ı����ݣ������ʽ���û�ָ��
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
	 * ����Զ�̷��������ص���Ϣ��Ĭ�ϱ����ʽΪUTF-8
	 * @return  String���͵���Ϣ����
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
	 * �ر���������
	 * @throws IOException
	 */
	public void close() throws IOException
	{
		input.close();
		output.close();
		socket.close();
	}
}
