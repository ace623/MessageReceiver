import seagosoft.iscas.socket.*;

public class MQConvertXML {
	private static String record1 = "1,无车牌,41,610191001000,新添大道与高新路交叉口,1,5201000000094801,02,0,2014-07-11 13:35:33,5201000000,1,ftp://hisense:hisense@52.1.101.210:21/kakou/201407/Hisense/610191001000/11/13/00071032042014071113353360902.jpg,,,,,01,0/0/1/1/1,0,,,,,,,,,,,0,,frame=4916087,,,,,,,,1";
	private static String record2 = "2,贵AGA917,02,610081007000,宝山北路与延安东路交叉口,1,5201000000104101,03,13,2014-07-11 14:49:15,5201000000,2,ftp://vion6:vion6@52.1.101.197:21/52.2.115.234/kk/2014-07-11/14491595000010237.jpg,,,12,";	

	private static ConvertHisenseMQ convertMQ;
	private static ProduceISCASPackage     iscasPack;
	
	public static void main(String[] args)
	{
		convertMQ = new ConvertHisenseMQ();
		iscasPack = new ProduceISCASPackage();
		
		byte[] pack = iscasPack.produceISCASPackage(0, convertMQ.convertPassingInfo(record2));
		String str = new String(pack);
		System.out.print(str);
		

	}

}
