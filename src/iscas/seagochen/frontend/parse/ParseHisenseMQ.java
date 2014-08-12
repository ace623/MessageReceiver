package iscas.seagochen.frontend.parse;

import iscas.seagochen.exceptions.UnknownStringException;


public class ParseHisenseMQ {
	public static enum xmlType {
		SG_DATA_SOURCE,     // ������Դ <XTBH>
		SG_DATA_TYPE,       // ���ݰ����� <Type>
		
		SG_VEHICLE_LICENSE, // ���ƺ��� <CarNo>
		SG_VEHICLE_SPEED,   // �����ٶ� <CarSpeed>
		SG_VEHICLE_DIRECT,  // ��ʻ���� <Direction>
		SG_VEHICLE_COLOR,   // ������ɫ <CarColor>
		SG_VEHICLE_LOGO,    // ������־ <CarLogo>
		
		SG_LICENSE_TYPE,    // �������� <CarType>
		SG_LICENSE_COLOR,   // ������ɫ <PlateColor>
		
		SG_ADDRESS_NO,      // �ɼ��ص� <AddNo>
		SG_ADDRESS_NAME,    // �ص����� <DeviceDesc>
		
		SG_CAPTURING_TYPE,  // ץ������ <UploadType>
		SG_CAPTURING_TIME,  // ץ��ʱ�� <WatchTime>
		
		SG_DEVICE_SN,       // �豸��� <DeviceNo>
		SG_MANUFACTURER_NO, // ���̱�� <WorksNo>
		
		SG_ROAD_NO,         // ������� <CarRoad>
		
		SG_ILLEGAL_CODE,    // Υ������ <Wfdm>
		
		SG_RED_ARISE_TIME,  // �������ʱ�� <StartTime>
		SG_RED_FALL_TIME,   // ��ƽ���ʱ�� <EndTime>
	};
	
	private String detectManufacturer( String record )
	{	
		if ( record.indexOf("hisense") > 0 )
		{
			return "<WorksNo>01</WorksNo>\n";
		}
		if ( record.indexOf("vion") > 0 )
		{
			return "<WorksNo>02</WorksNo>\n";
		}
		if ( record.indexOf("pic") > 0 )
		{
			return "<WorksNo>03</WorksNo>\n";
		}
		
		return "<WorksNo>10</WorksNo>\n";
	}
	
	/**
	 * ����Ƭ���ӵ�ַת��ΪXML��ʽ���ַ���
	 * @param pictureUrls ������һ����һ�����ϵ���Ч��ַ������Ϣ
	 * @return            ת��ΪXML��ʽ����ַ���
	 */
 	public String convertToXMLUrls( String[] pictureUrls )
	{
		String pictUrls = null;
		String items = null;
		if ( pictureUrls.length < 1 || pictureUrls.length > 3 )
		{
			pictUrls = "<PicNum>0</PicNum>\n";
			return pictUrls;
		}
		
		items = "<Picture>\n";
		int i = 1;
		for (; i <= pictureUrls.length; i++ )
		{
			if ( "".equals(pictureUrls[i-1]) || null == pictureUrls[i-1] )
				break;

			items += "<PicUrl" + i + ">" + pictureUrls[i-1] + 
					"</PicUrl" + i + ">\n";			
		}		
		items += "</Picture>\n";
		
		if ( i > 1 )
		{
			i--;
			pictUrls = "<PicNum>" + i + "</PicNum>\n" + items;
		}
		else
		{
			pictUrls = "<PicNum>0</PicNum>\n";
		}
			
		
		return pictUrls;
	}

 	/**
 	 * ����Ƭ���ӣ���Ƶ���Ӵ���ΪXML��ʽ���ַ���
 	 * @param pcitureUrls
 	 * @param videlUrl
 	 * @return
 	 */
 	public String convertToXMLUrls( String[] pcitureUrls, String videoUrl )
 	{
 		String UrlsString = convertToXMLUrls(pcitureUrls);
 		
 		return UrlsString + "<VideoUrl>" + videoUrl + "</VideoUrl>\n";
 	}
	
	/**
	 * ����һ���ת��ΪXML��ʽ���ַ���
	 * @param item      ������
	 * @param packType  ����������Ӧ��XML���
	 * @return          ת��ΪXML��ʽ����ַ���
	 */
	public String convertToXMLMark( String item, xmlType packType )
	{
		if ( "".equals(item) ) return item;
		
		switch ( packType )
		{
		case SG_DATA_SOURCE:
			return "<XTBH>" + item + "</XTBH>\n";
		case SG_DATA_TYPE:
			return "<Type>" + item + "</Type>\n";
			
		case SG_VEHICLE_LICENSE:
			if ( "�޳���".equals(item) || "����".equals(item) || 
					"δʶ��".equals(item) )
				return "<CarNo>?</CarNo>\n";
			return "<CarNo>" + item + "</CarNo>\n";
		case SG_VEHICLE_SPEED:
			return "<CarSpeed>" + item + "</CarSpeed>\n";
		case SG_VEHICLE_DIRECT:
			return "<Direction>" + item + "</Direction>\n";
		case SG_VEHICLE_COLOR:
			return "<CarColor>" + item + "</CarColor>\n";
		case SG_VEHICLE_LOGO:
			return "<CarLogo>" + item + "</CarLogo>\n";
		
		case SG_LICENSE_TYPE:
			return "<CarType>" + item + "</CarType>\n";
		case SG_LICENSE_COLOR:
			return "<PlateColor>" + item + "</PlateColor>\n";
		
		case SG_ADDRESS_NO:
			return "<AddNo>" + item + "</AddNo>\n";
		case SG_ADDRESS_NAME:
			return "<DeviceDesc>" + item + "</DeviceDesc>\n";
		
		case SG_CAPTURING_TYPE:
			return "<UploadType>" + item + "</UploadType>\n";
		case SG_CAPTURING_TIME:
			return "<WatchTime>" + item + "</WatchTime>\n" +
		"<WatchTime1>" + item + "</WatchTime1>\n" +
		"<WatchTime2>?</WatchTime2>\n" +
		"<WatchTime3>?</WatchTime3>\n" +
		"<WatchTime4>?</WatchTime4>\n";
			
		case SG_DEVICE_SN:
			return "<DeviceNo>" + item + "</DeviceNo>\n";
		
		case SG_ROAD_NO:
			return "<CarRoad>" + item + "</CarRoad>\n";
		
		case SG_ILLEGAL_CODE:
			return "<Wfdm>" + item + "</Wfdm>\n";
		
		case SG_RED_ARISE_TIME:
			return "<StartTime>" + item + "</StartTime>\n";
		case SG_RED_FALL_TIME:
			return "<EndTime>" + item + "</EndTime>\n"; 
			
			default : break;
		}
		
		return "<None>" + item + "</None>";
	}

	/**
	 * ������MQ�л�õ�ͨ������ת��ΪXML��ʽ���ַ���
	 * @param record  ����ͨ����¼
	 * @return        ת��ΪXML��ʽ���ַ����������������͡��ļ������Լ�XML�ļ�ʵ��
	 * @throws UnknownStringException 
	 */
	public String convertPassingInfo( String record ) throws UnknownStringException
	{
		String xmlPackage = null;
		String records[] = record.split(",");
		
		if (records.length < 16)
		{
			System.out.println( "error> " + record );
			throw new UnknownStringException("cannot parse the input string");
		}
		
		// �����ļ��������ͼ��ļ���
		String xmlInfo = "KAKOU_" + records[6] + ".XML"; // KAKOU_<device number>
		if ( xmlInfo.length() < 57 )
			for ( int i = xmlInfo.length(); i < 57; i++ ) xmlInfo += " ";
		xmlInfo = "01" + xmlInfo;
		
		// ����XML�ļ�ͷ
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
				"<Package>\n<PackageHead>\n<Version>1.0</Version>\n<Record>1</Record>\n<Desc></Desc>\n</PackageHead>\n";
		String xmlData = null;
		
		// �������ݣ�����ʼת����ʽ
		String[] subUrls = {records[12], records[13], records[14]}; // ��ƬURL
		
		xmlData =
				convertToXMLMark( "Z", xmlType.SG_DATA_TYPE ) +              // �������ͣ���������
				detectManufacturer( records[12] ) +                  // ���̱��
				convertToXMLMark( records[0], xmlType.SG_DATA_SOURCE ) +     // ������Դ
				convertToXMLMark( records[1], xmlType.SG_VEHICLE_LICENSE ) + // ���ƺ���
				convertToXMLMark( records[2], xmlType.SG_LICENSE_TYPE ) +    // ��������
				convertToXMLMark( records[3], xmlType.SG_ADDRESS_NO ) +      // �ص��ţ�12λ
				convertToXMLMark( records[4], xmlType.SG_ADDRESS_NAME ) +    // �ص�����
				convertToXMLMark( records[5], xmlType.SG_CAPTURING_TYPE ) +  // ץ������
				convertToXMLMark( records[6], xmlType.SG_DEVICE_SN ) +       // �豸��ţ�16λ
				convertToXMLMark( records[7], xmlType.SG_ROAD_NO ) +         // �������
				convertToXMLMark( records[8], xmlType.SG_VEHICLE_SPEED ) +   // �����ٶ�
				convertToXMLMark( records[9], xmlType.SG_CAPTURING_TIME ) +  // ץ��ʱ��
				convertToXMLMark( records[11], xmlType.SG_VEHICLE_DIRECT ) + // ��������
				convertToXMLUrls( subUrls ) +                        // ��Ƭ����  
				convertToXMLMark( records[15], xmlType.SG_VEHICLE_COLOR );   // ������ɫ
		
		if ( records.length > 16 )
			xmlData += 
				convertToXMLMark( records[16], xmlType.SG_VEHICLE_LOGO ) +   // ������־
				convertToXMLMark( records[19], xmlType.SG_LICENSE_COLOR );   // ������ɫ
		
		// �������յ��ļ�����
		xmlPackage = xmlInfo + xmlHeader + "<Data>\n" + xmlData + "</Data>\n</Package>\n\r\n";
		
		return xmlPackage;
	}

	
	/**
	 * ������MQ�л�õ�Υ������ת��ΪXML��ʽ���ַ���
	 * @param record   ����Υ����¼
	 * @return         ת��ΪXML��ʽ���ַ����������������͡��ļ������Լ�XML�ļ�ʵ��
	 * @throws UnknownStringException 
	 */
	public String  convertIllegalInfo( String record ) throws UnknownStringException
	{
		String xmlPackage = null;
		String records[] = record.split(",");
		
		if ( records.length < 19 )
		{
			System.out.println( "error> " + record );
			throw new UnknownStringException("cannot parse the input string");
		}
		
		// �����ļ��������ͼ��ļ���
		String xmlInfo = "ILLEGAL_" + records[10] + ".XML"; // KAKOU_<device number>
		if ( xmlInfo.length() < 57 )
			for ( int i = xmlInfo.length(); i < 57; i++ ) xmlInfo += " ";
		xmlInfo = "02" + xmlInfo;
		
		// ����XML�ļ�ͷ
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
				"<Package>\n<PackageHead>\n<Version>1.0</Version>\n<Record>1</Record>\n<Desc></Desc>\n</PackageHead>\n";
		String xmlData = null;
		
		// �������ݣ�����ʼת����ʽ
		String[] subUrls = {records[15], records[16], records[17]}; // ��ƬURL
		
		xmlData = 
				convertToXMLMark( "B", xmlType.SG_DATA_TYPE ) +              // �������ͣ������
				detectManufacturer( records[15] ) +                  // ���̱��
				convertToXMLMark( records[1], xmlType.SG_LICENSE_TYPE ) +    // ��������
				convertToXMLMark( records[2], xmlType.SG_VEHICLE_LICENSE ) + // ��������
				convertToXMLMark( records[3], xmlType.SG_CAPTURING_TIME ) +  // ץ��ʱ��
				convertToXMLMark( records[4], xmlType.SG_ILLEGAL_CODE ) +    // Υ������
				convertToXMLMark( records[5], xmlType.SG_ADDRESS_NO ) +      // �ص��ţ�12λ
				convertToXMLMark( records[6], xmlType.SG_ADDRESS_NAME ) +    // �ص�����
				convertToXMLMark( records[8], xmlType.SG_DATA_SOURCE ) +     // ������Դ
				convertToXMLMark( records[9], xmlType.SG_CAPTURING_TYPE ) +  // ץ������
				convertToXMLMark( records[10], xmlType.SG_DEVICE_SN ) +      // �豸��ţ�16λ
				convertToXMLMark( records[11], xmlType.SG_VEHICLE_DIRECT ) + // ��������
				convertToXMLMark( records[12], xmlType.SG_ROAD_NO ) +        // �������
				convertToXMLMark( records[13], xmlType.SG_RED_ARISE_TIME ) + // �������ʱ��
				convertToXMLMark( records[14], xmlType.SG_RED_FALL_TIME ) +  // ���Ϩ��ʱ��
				convertToXMLUrls( subUrls, records[18] );
		
		if ( records.length > 20 && !"".equals( records[20]) )
			xmlData += convertToXMLMark( records[20], xmlType.SG_LICENSE_COLOR ); // ������ɫ

		
		// �������յ��ļ�����
		xmlPackage = xmlInfo + xmlHeader + "<Data>\n" + xmlData + "</Data>\n</Package>\n\r\n";
		
		return xmlPackage;
	}
}
