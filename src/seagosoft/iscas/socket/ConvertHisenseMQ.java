package seagosoft.iscas.socket;

public class ConvertHisenseMQ
{
	public static final int SG_DATA_SOURCE = 0; // ������Դ <XTBH>
	public static final int SG_DATA_TYPE   = 1; // ���ݰ����� <Type>
	
	public static final int SG_VEHICLE_LICENSE = 10; // ���ƺ��� <CarNo>
	public static final int SG_VEHICLE_SPEED   = 11; // �����ٶ� <CarSpeed>
	public static final int SG_VEHICLE_DIRECT  = 12; // ��ʻ���� <Direction>
	public static final int SG_VEHICLE_COLOR   = 13; // ������ɫ <CarColor>
	public static final int SG_VEHICLE_LOGO    = 14; // ������־ <CarLogo>
	
	public static final int SG_LICENSE_TYPE  = 20; // �������� <CarType>
	public static final int SG_LICENSE_COLOR = 21; // ������ɫ <PlateColor>
	
	public static final int SG_ADDRESS_NO   = 30; // �ɼ��ص� <AddNo>
	public static final int SG_ADDRESS_NAME = 31; // �ص����� <DeviceDesc>
	
	public static final int SG_CAPTURING_TYPE = 40; // ץ������ <UploadType>
	public static final int SG_CAPTURING_TIME = 41; // ץ��ʱ�� <WatchTime>
	
	public static final int SG_DEVICE_SN  = 50; // �豸��� <DeviceNo>
	
	public static final int SG_ROAD_NO    = 60; // ������� <CarRoad>
	
	public static final int SG_ILLEGAL_CODE = 70; // Υ������ <Wfdm>
	
	public static final int SG_RED_ARISE_TIME = 80; // �������ʱ�� <StartTime>
	public static final int SG_RED_FALL_TIME  = 81; // ��ƽ���ʱ�� <EndTime>
	
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
	 * ����һ���ת��ΪXML��ʽ���ַ���
	 * @param item      ������
	 * @param packType  ����������Ӧ��XML���
	 * @return          ת��ΪXML��ʽ����ַ���
	 */
	public String convertToXMLMark( String item, int packType )
	{
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
		"<WatchTime1>" + item + "</WatchTime>\n" +
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
		
		return "<none>" + item + "</none>";
	}

	/**
	 * ������MQ�л�õ�ͨ������ת��ΪXML��ʽ���ַ���
	 * @param record  ����ͨ����¼
	 * @return        ת��ΪXML��ʽ���ַ����������������͡��ļ������Լ�XML�ļ�ʵ��
	 */
	public String convertPassingInfo( String record )
	{
		String xmlPackage = null;
		String records[] = record.split(",");
		
		// �����ļ��������ͼ��ļ���
		String xmlInfo = "KAKOU_" + records[6] + ".XML"; // KAKOU_<device number>
		if ( xmlInfo.length() < 57 )
			for ( int i = xmlInfo.length(); i < 57; i++ ) xmlInfo += " ";
		xmlInfo = "01" + xmlInfo;
		
		// ����XML�ļ�ͷ
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
				"<PackageHead>\n<Version>1.0</Version>\n<Record>1</Record>\n<Desc></Desc>\n</PackageHead>\n";
		String xmlData = null;
		
		// �������ݣ�����ʼת����ʽ
		String[] subUrls = {records[12], records[13], records[14]}; // ��ƬURL
		
		xmlData = convertToXMLMark( records[0], SG_DATA_SOURCE ) +
				convertToXMLMark( "Z", SG_DATA_TYPE ) +
				convertToXMLMark( records[1], SG_VEHICLE_LICENSE ) +
				convertToXMLMark( records[2], SG_LICENSE_TYPE ) +
				convertToXMLMark( records[3], SG_ADDRESS_NO ) +
				convertToXMLMark( records[4], SG_ADDRESS_NAME ) +
				convertToXMLMark( records[5], SG_CAPTURING_TYPE ) +
				convertToXMLMark( records[6], SG_DEVICE_SN ) +
				convertToXMLMark( records[7], SG_ROAD_NO ) +
				convertToXMLMark( records[8], SG_VEHICLE_SPEED ) +
				convertToXMLMark( records[9], SG_CAPTURING_TIME ) +
				convertToXMLMark( records[11], SG_VEHICLE_DIRECT ) +
				convertToXMLUrls( subUrls ) + // picture URLs 
				convertToXMLMark( records[15], SG_VEHICLE_COLOR );
		
		if ( records.length > 16 )
			xmlData += convertToXMLMark( records[16], SG_VEHICLE_LOGO ) +
				convertToXMLMark( records[19], SG_LICENSE_COLOR );
		
		// �������յ��ļ�����
		xmlPackage = xmlInfo + xmlHeader + "<Package>\n<Data>\n" + xmlData + "</Data>\n</Package>\n";
		
		return xmlPackage;
	}

	
	/**
	 * ������MQ�л�õ�Υ������ת��ΪXML��ʽ���ַ���
	 * @param record   ����Υ����¼
	 * @return         ת��ΪXML��ʽ���ַ����������������͡��ļ������Լ�XML�ļ�ʵ��
	 */
	public String packIllegalInfo( String record )
	{
		String xmlPackage = null;
		String records[] = record.split(",");
		//TODO
		String xmlInfo = null;
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
				"<PackageHead>\n<Version>1.0</Version>\n<Record>1</Record>\n<Desc></Desc>\n</PackageHead>\n";
		String xmlData = null;
		
		// �������ݣ���ת����ʽ
		//TODO
		
		// �������յ�XML�ļ�
		xmlPackage = xmlInfo + "<Package>\n" + xmlHeader + "<Data>" + xmlData + "</Data>" + "</Package>\n";
		
		return xmlPackage;		
	}

}