package seagosoft.iscas.tester;

import seagosoft.iscas.socket.ConvertHisenseMQ;
import seagosoft.iscas.socket.ParseISCASPackage;
import seagosoft.iscas.socket.ProduceISCASPackage;

public class HisenseMQ {

	private static ConvertHisenseMQ converter;
	private static ProduceISCASPackage producer;
	private static ParseISCASPackage parse;
	
	private static String passRecord1 = "1,无车牌,41,610191001000,新添大道与高新路交叉口,1,5201000000094801,02,0,2014-07-11 13:35:33,5201000000,1,ftp://hisense:hisense@52.1.101.210:21/kakou/201407/Hisense/610191001000/11/13/00071032042014071113353360902.jpg,,,,,01,0/0/1/1/1,0,,,,,,,,,,,0,,frame=4916087,,,,,,,,1";
	private static String illegalRecord1 = "1,02,贵VLTFJL,2014-07-14 17:53:49,54321,710171001000,永乐路与合群路交叉口,5201000000,1,1,5201000000061311,1,04,,,ftp://vion3:vion3@52.1.101.198:21/52.2.108.14/xsxx/2014-07-14/201407141753492901.jpg,ftp://vion3:vion3@52.1.101.198:21/52.2.108.14/xsxx/2014-07-14/201407141753492902.jpg,ftp://vion3:vion3@52.1.101.198:21/52.2.108.14/xsxx/2014-07-14/201407141753492903.jpg,ftp://vion3:vion3@52.1.101.198:21/52.2.108.14/xsxx/2014-07-14/201407141753492901.avi,0/0/0/0/0";
	private static String illegalRecord2 = "1,02,贵A1288P,2014-07-14 17:53:56,82070,601561008000,甲秀南路与新区大道交叉口,5201000000,1,1,5201000000097401,4,03,,0,ftp://hisense:hisense@52.1.101.211:21/weifa/201407/Hisense/601561008000/14/17/0006103144201407141753567790301.jpg,ftp://hisense:hisense@52.1.101.211:21/weifa/201407/Hisense/601561008000/14/17/0006103144201407141753569140302.jpg,ftp://hisense:hisense@52.1.101.211:21/weifa/201407/Hisense/601561008000/14/17/0006103144201407141753570620303.jpg,ftp://hisense:hisense@52.1.101.211:21/weifa/201407/Hisense/601561008000/14/17/0006103144201407141753567790301.mp4,915/1593/126/42/1,1,,,,,,,,,,1,不按道行驶,,,,,,,,,1,10";
	
	private static void printIllegal( ConvertHisenseMQ converter, ProduceISCASPackage producer, ParseISCASPackage parse,  String record )
	{
		byte[] tokens = null;
		try {
			String message = converter.convertIllegalInfo(record);
			tokens = producer.produceISCASPackage(0, message);
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		parse.read(tokens);
		parse.print();
	}
	
	private static void printPassing( ConvertHisenseMQ converter, ProduceISCASPackage producer, ParseISCASPackage parse,  String record )
	{
		byte[] tokens = null;
		try {
			String message = converter.convertPassingInfo(record);
			tokens = producer.produceISCASPackage(0, message);
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
		
		parse.read(tokens);
		parse.print();
	}	
	
	public static void main(String[] argv)
	{
		converter = new ConvertHisenseMQ();
		producer  = new ProduceISCASPackage();
		parse     = new ParseISCASPackage();
		
		printPassing(converter, producer, parse, passRecord1);
		printIllegal(converter, producer, parse, illegalRecord1);
		printIllegal(converter, producer, parse, illegalRecord2);
	}
}
