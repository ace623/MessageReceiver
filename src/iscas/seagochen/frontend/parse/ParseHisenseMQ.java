package iscas.seagochen.frontend.parse;

import iscas.seagochen.exceptions.UnknownStringException;


public class ParseHisenseMQ {
	public static enum xmlType {
		SG_DATA_SOURCE,     // 数据来源 <XTBH>
		SG_DATA_TYPE,       // 数据包类型 <Type>
		
		SG_VEHICLE_LICENSE, // 车牌号码 <CarNo>
		SG_VEHICLE_SPEED,   // 车辆速度 <CarSpeed>
		SG_VEHICLE_DIRECT,  // 行驶方向 <Direction>
		SG_VEHICLE_COLOR,   // 车身颜色 <CarColor>
		SG_VEHICLE_LOGO,    // 车辆标志 <CarLogo>
		
		SG_LICENSE_TYPE,    // 车牌类型 <CarType>
		SG_LICENSE_COLOR,   // 车牌颜色 <PlateColor>
		
		SG_ADDRESS_NO,      // 采集地点 <AddNo>
		SG_ADDRESS_NAME,    // 地点名称 <DeviceDesc>
		
		SG_CAPTURING_TYPE,  // 抓拍类型 <UploadType>
		SG_CAPTURING_TIME,  // 抓拍时间 <WatchTime>
		
		SG_DEVICE_SN,       // 设备编号 <DeviceNo>
		SG_MANUFACTURER_NO, // 厂商编号 <WorksNo>
		
		SG_ROAD_NO,         // 车道编号 <CarRoad>
		
		SG_ILLEGAL_CODE,    // 违法代码 <Wfdm>
		
		SG_RED_ARISE_TIME,  // 红灯亮起时间 <StartTime>
		SG_RED_FALL_TIME,   // 红灯结束时间 <EndTime>
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
 	 * 将照片链接，视频链接处理为XML格式的字符串
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
	 * 将数一般据转换为XML格式的字符串
	 * @param item      数据项
	 * @param packType  与该数据项对应的XML标记
	 * @return          转换为XML格式后的字符串
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
	 * 将海信MQ中获得的通车数据转换为XML格式的字符串
	 * @param record  单条通车记录
	 * @return        转换为XML格式的字符串，包含数据类型、文件名，以及XML文件实体
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
		
		// 生成文件数据类型及文件名
		String xmlInfo = "KAKOU_" + records[6] + ".XML"; // KAKOU_<device number>
		if ( xmlInfo.length() < 57 )
			for ( int i = xmlInfo.length(); i < 57; i++ ) xmlInfo += " ";
		xmlInfo = "01" + xmlInfo;
		
		// 生成XML文件头
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
				"<Package>\n<PackageHead>\n<Version>1.0</Version>\n<Record>1</Record>\n<Desc></Desc>\n</PackageHead>\n";
		String xmlData = null;
		
		// 解析数据，并开始转换格式
		String[] subUrls = {records[12], records[13], records[14]}; // 照片URL
		
		xmlData =
				convertToXMLMark( "Z", xmlType.SG_DATA_TYPE ) +              // 数据类型，卡口数据
				detectManufacturer( records[12] ) +                  // 厂商编号
				convertToXMLMark( records[0], xmlType.SG_DATA_SOURCE ) +     // 数据来源
				convertToXMLMark( records[1], xmlType.SG_VEHICLE_LICENSE ) + // 车牌号码
				convertToXMLMark( records[2], xmlType.SG_LICENSE_TYPE ) +    // 车牌类型
				convertToXMLMark( records[3], xmlType.SG_ADDRESS_NO ) +      // 地点编号，12位
				convertToXMLMark( records[4], xmlType.SG_ADDRESS_NAME ) +    // 地点名称
				convertToXMLMark( records[5], xmlType.SG_CAPTURING_TYPE ) +  // 抓拍类型
				convertToXMLMark( records[6], xmlType.SG_DEVICE_SN ) +       // 设备编号，16位
				convertToXMLMark( records[7], xmlType.SG_ROAD_NO ) +         // 车道编号
				convertToXMLMark( records[8], xmlType.SG_VEHICLE_SPEED ) +   // 车辆速度
				convertToXMLMark( records[9], xmlType.SG_CAPTURING_TIME ) +  // 抓拍时间
				convertToXMLMark( records[11], xmlType.SG_VEHICLE_DIRECT ) + // 车辆方向
				convertToXMLUrls( subUrls ) +                        // 照片链接  
				convertToXMLMark( records[15], xmlType.SG_VEHICLE_COLOR );   // 车辆颜色
		
		if ( records.length > 16 )
			xmlData += 
				convertToXMLMark( records[16], xmlType.SG_VEHICLE_LOGO ) +   // 车辆标志
				convertToXMLMark( records[19], xmlType.SG_LICENSE_COLOR );   // 拍照颜色
		
		// 生成最终的文件数据
		xmlPackage = xmlInfo + xmlHeader + "<Data>\n" + xmlData + "</Data>\n</Package>\n\r\n";
		
		return xmlPackage;
	}

	
	/**
	 * 将海信MQ中获得的违法数据转换为XML格式的字符串
	 * @param record   单条违法记录
	 * @return         转换为XML格式的字符串，包含数据类型、文件名，以及XML文件实体
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
		
		// 生成文件数据类型及文件名
		String xmlInfo = "ILLEGAL_" + records[10] + ".XML"; // KAKOU_<device number>
		if ( xmlInfo.length() < 57 )
			for ( int i = xmlInfo.length(); i < 57; i++ ) xmlInfo += " ";
		xmlInfo = "02" + xmlInfo;
		
		// 生成XML文件头
		String xmlHeader = "<?xml version=\"1.0\" encoding=\"GBK\"?>\n" +
				"<Package>\n<PackageHead>\n<Version>1.0</Version>\n<Record>1</Record>\n<Desc></Desc>\n</PackageHead>\n";
		String xmlData = null;
		
		// 解析数据，并开始转换格式
		String[] subUrls = {records[15], records[16], records[17]}; // 照片URL
		
		xmlData = 
				convertToXMLMark( "B", xmlType.SG_DATA_TYPE ) +              // 数据类型，闯红灯
				detectManufacturer( records[15] ) +                  // 厂商编号
				convertToXMLMark( records[1], xmlType.SG_LICENSE_TYPE ) +    // 号牌类型
				convertToXMLMark( records[2], xmlType.SG_VEHICLE_LICENSE ) + // 车辆号牌
				convertToXMLMark( records[3], xmlType.SG_CAPTURING_TIME ) +  // 抓拍时间
				convertToXMLMark( records[4], xmlType.SG_ILLEGAL_CODE ) +    // 违法代码
				convertToXMLMark( records[5], xmlType.SG_ADDRESS_NO ) +      // 地点编号，12位
				convertToXMLMark( records[6], xmlType.SG_ADDRESS_NAME ) +    // 地点名称
				convertToXMLMark( records[8], xmlType.SG_DATA_SOURCE ) +     // 数据来源
				convertToXMLMark( records[9], xmlType.SG_CAPTURING_TYPE ) +  // 抓拍类型
				convertToXMLMark( records[10], xmlType.SG_DEVICE_SN ) +      // 设备编号，16位
				convertToXMLMark( records[11], xmlType.SG_VEHICLE_DIRECT ) + // 车辆方向
				convertToXMLMark( records[12], xmlType.SG_ROAD_NO ) +        // 车道编号
				convertToXMLMark( records[13], xmlType.SG_RED_ARISE_TIME ) + // 红灯亮起时间
				convertToXMLMark( records[14], xmlType.SG_RED_FALL_TIME ) +  // 红灯熄灭时间
				convertToXMLUrls( subUrls, records[18] );
		
		if ( records.length > 20 && !"".equals( records[20]) )
			xmlData += convertToXMLMark( records[20], xmlType.SG_LICENSE_COLOR ); // 车牌颜色

		
		// 生成最终的文件数据
		xmlPackage = xmlInfo + xmlHeader + "<Data>\n" + xmlData + "</Data>\n</Package>\n\r\n";
		
		return xmlPackage;
	}
}
