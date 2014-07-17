package seagosoft.iscas.socket;

public class ConvertHisenseMQ
{
	public static final int SG_DATA_SOURCE = 0; // 数据来源 <XTBH>
	public static final int SG_DATA_TYPE   = 1; // 数据包类型 <Type>
	
	public static final int SG_VEHICLE_LICENSE = 10; // 车牌号码 <CarNo>
	public static final int SG_VEHICLE_SPEED   = 11; // 车辆速度 <CarSpeed>
	public static final int SG_VEHICLE_DIRECT  = 12; // 行驶方向 <Direction>
	public static final int SG_VEHICLE_COLOR   = 13; // 车身颜色 <CarColor>
	public static final int SG_VEHICLE_LOGO    = 14; // 车辆标志 <CarLogo>
	
	public static final int SG_LICENSE_TYPE  = 20; // 车牌类型 <CarType>
	public static final int SG_LICENSE_COLOR = 21; // 车牌颜色 <PlateColor>
	
	public static final int SG_ADDRESS_NO   = 30; // 采集地点 <AddNo>
	public static final int SG_ADDRESS_NAME = 31; // 地点名称 <DeviceDesc>
	
	public static final int SG_CAPTURING_TYPE = 40; // 抓拍类型 <UploadType>
	public static final int SG_CAPTURING_TIME = 41; // 抓拍时间 <WatchTime>
	
	public static final int SG_DEVICE_SN  = 50; // 设备编号 <DeviceNo>
	
	public static final int SG_ROAD_NO    = 60; // 车道编号 <CarRoad>
	
	public static final int SG_ILLEGAL_CODE = 70; // 违法代码 <Wfdm>
	
	public static final int SG_RED_ARISE_TIME = 80; // 红灯亮起时间 <StartTime>
	public static final int SG_RED_FALL_TIME  = 81; // 红灯结束时间 <EndTime>
	
	/**
	 * 将照片链接地址转换为XML格式的字符串
	 * @param pictureUrls 包含有一条或一条以上的有效地址链接信息
	 * @return            转换为XML格式后的字符串
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
	 * 将数一般据转换为XML格式的字符串
	 * @param item      数据项
	 * @param packType  与该数据项对应的XML标记
	 * @return          转换为XML格式后的字符串
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
			if ( "无车牌".equals(item) || "无牌".equals(item) || 
					"未识别".equals(item) )
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
	 * 将海信MQ中获得的通车数据转换为XML格式的字符串
	 * @param record  单条通车记录
	 * @return        转换为XML格式的字符串，包含数据类型、文件名，以及XML文件实体
	 */
	public String convertPassingInfo( String record )
	{
		String xmlPackage = null;
		String records[] = record.split(",");
		
		// 生成文件数据类型及文件名
		String xmlInfo = "KAKOU_" + records[6] + ".XML"; // KAKOU_<device number>
		if ( xmlInfo.length() < 57 )
			for ( int i = xmlInfo.length(); i < 57; i++ ) xmlInfo += " ";
		xmlInfo = "01" + xmlInfo;
		
		// 生成XML文件头
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
				"<PackageHead>\n<Version>1.0</Version>\n<Record>1</Record>\n<Desc></Desc>\n</PackageHead>\n";
		String xmlData = null;
		
		// 解析数据，并开始转换格式
		String[] subUrls = {records[12], records[13], records[14]}; // 照片URL
		
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
		
		// 生成最终的文件数据
		xmlPackage = xmlInfo + xmlHeader + "<Package>\n<Data>\n" + xmlData + "</Data>\n</Package>\n";
		
		return xmlPackage;
	}

	
	/**
	 * 将海信MQ中获得的违法数据转换为XML格式的字符串
	 * @param record   单条违法记录
	 * @return         转换为XML格式的字符串，包含数据类型、文件名，以及XML文件实体
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
		
		// 解析数据，并转换格式
		//TODO
		
		// 生成最终的XML文件
		xmlPackage = xmlInfo + "<Package>\n" + xmlHeader + "<Data>" + xmlData + "</Data>" + "</Package>\n";
		
		return xmlPackage;		
	}

}