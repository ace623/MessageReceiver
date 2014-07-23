package seagosoft.iscas.tester;

import java.io.*; 
import java.net.*; 

import seagosoft.iscas.exception.UnknownStringException;
import seagosoft.iscas.socket.ConvertHisenseMQ;
import seagosoft.iscas.socket.ParseISCASPackage;
import seagosoft.iscas.socket.ProduceISCASPackage;

public class SocketClient {
	
	private static String record1 = "1,无车牌,41,610191001000,新添大道与高新路交叉口,1,5201000000094801,02,0,2014-07-11 13:35:33,5201000000,1,ftp://hisense:hisense@52.1.101.210:21/kakou/201407/Hisense/610191001000/11/13/00071032042014071113353360902.jpg,,,,,01,0/0/1/1/1,0,,,,,,,,,,,0,,frame=4916087,,,,,,,,1";
	private static String record2 = "2,贵AGA917,02,610081007000,宝山北路与延安东路交叉口,1,5201000000104101,03,13,2014-07-11 14:49:15,5201000000,2,ftp://vion6:vion6@52.1.101.197:21/52.2.115.234/kk/2014-07-11/14491595000010237.jpg,,,12,";	

	private static ConvertHisenseMQ    convertMQ;
	private static ProduceISCASPackage iscasPack;
	
	private static byte[] tokens1;
	private static byte[] tokens2;
	
	Socket socket;
	DataOutputStream output;
	DataInputStream input;
	
	public SocketClient()
	{
		final String serverAddress = "52.1.126.70";
		//final String serverAddress = "10.1.2.132";
		final int port = 12351;
		
		try {
			init();
		} catch (UnknownStringException e) {
			e.printStackTrace();
		}
		
		try {
			socket = new Socket( serverAddress, port );
			socket.setSoTimeout(5000);
			input = new DataInputStream(socket.getInputStream());
			output = new DataOutputStream(socket.getOutputStream());
		}
		catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		
		try {
			if ( null == socket || null == input || null == output )
				throw new NullPointerException();
		} catch( NullPointerException e )
		{
			e.printStackTrace();
		}
			
		if ( !socket.isConnected() )
			System.out.println( "connection failed!" );
			
		try {
//			System.out.println( new String(tokens1) );
			output.write(tokens1);
			output.flush();
			System.out.println( "write success" );
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			byte[] tokens = new byte[256];
			int n = input.read(tokens);
			
			System.out.println( n );
			
			System.out.println( "success > " + new String(tokens) );
			
			output.close();
			input.close();
			socket.close();
		} catch ( IOException e )
		{
			e.printStackTrace();
		}
		
		ParseISCASPackage parsePack = new ParseISCASPackage();
		parsePack.read(tokens1);
		parsePack.print();
		
//		System.out.println( parsePack.checkValid() );
	}
	
	private void init() throws UnknownStringException
	{
		convertMQ = new ConvertHisenseMQ();
		iscasPack = new ProduceISCASPackage();
		
		try {
			tokens1 = iscasPack.produceISCASPackage(0, convertMQ.convertPassingInfo(record1));
			tokens2 = iscasPack.produceISCASPackage(0, convertMQ.convertPassingInfo(record2));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}		
	}
	
	public void print()
	{
		System.out.println("--------------------");
		System.out.println( new String(tokens1) );
		System.out.println("--------------------");
		System.out.println( new String(tokens2) );
		System.out.println("--------------------");
	}
	
	public static void main(String[] args)
	{
		new SocketClient();
		System.out.println( "exit..." );
	}

}
