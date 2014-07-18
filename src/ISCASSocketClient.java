import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
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
	
	public void SetSocketClient( ConvertHisenseMQ convert, ProduceISCASPackage producer )
	{
		this.converter = convert;
		this.producer = producer;
	}
	
	public void connect( String servAddr, int port, int millseconds )
	{
		try {
			
			socket = new Socket( servAddr, port );
			socket.setSoTimeout(millseconds);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}		
	}
	
	public void connect( String servAddr, int port )
	{
		try {
			
			socket = new Socket( servAddr, port );
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
			
		} catch (UnknownHostException e) {
			
			e.printStackTrace();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
	}

	
	/**
	 * ��MQ��Ϣת��֮���͸�������
	 * @param mq     ����MQ����Ϣ
	 * @param type   �������ͣ���������(0)�����������(1) 
	 */
	public boolean send( String mq, int times, int type )
	{
		try {
			
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
			
		} catch ( Exception e ) {
			e.printStackTrace();
		}
		
		try {
			
			output.write(sendTokens);
			output.flush();
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String recv()
	{
		String str = "";
		
		try {
			
			int recv = input.read(recvTokens);
			
			if ( recv < 0 )
			{
				System.out.println( "received failed" );
				return str;
			}
			
			str = new String( recvTokens, "UTF-8" );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return str;
	}
}
