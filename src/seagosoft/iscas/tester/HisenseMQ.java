package seagosoft.iscas.tester;

import java.io.UnsupportedEncodingException;

import seagosoft.iscas.socket.*;

public class HisenseMQ {

	private static ConvertHisenseMQ converter;
	private static ProduceISCASPackage producer;
	private static ParseISCASPackage parse;
	
	private static String passRecord1 = "1,无车牌,41,610191001000,新添大道与高新路交叉口,1,5201000000094801,02,0,2014-07-11 13:35:33,5201000000,1,ftp://hisense:hisense@52.1.101.210:21/kakou/201407/Hisense/610191001000/11/13/00071032042014071113353360902.jpg,,,,,01,0/0/1/1/1,0,,,,,,,,,,,0,,frame=4916087,,,,,,,,1";
	
	public static void main(String[] argv)
	{
		converter = new ConvertHisenseMQ();
		producer  = new ProduceISCASPackage();
		parse     = new ParseISCASPackage();
		
		byte[] tokens = null;
		try {
			String message = converter.convertPassingInfo(passRecord1);
			tokens  = producer.produceISCASPackage(0, message);
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		System.out.println( "token size: " + tokens.length );
		parse.read(tokens);
		parse.print();
		try {
			System.out.println( parse.checkValid() );
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}	
}
