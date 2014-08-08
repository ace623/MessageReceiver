package iscas.seagochen.format;

import java.io.UnsupportedEncodingException;

public class ProduceISCASPackage {
	
	/**
	 * 将生成的消息记录按照内部数据包格式进行转换，并将编码转换为UTF-8格式
	 * @param times  数据发送次数
	 * @param record 海信MQ，认为该数据中已包含数据类型、标题、XML数据、数据包尾\r\n
	 * @return       用于socket字节串
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] produceISCASPackage( int times, String record ) throws UnsupportedEncodingException
	{	
		int xmlLength = record.getBytes("UTF-8").length - 61;
		int length = record.getBytes("UTF-8").length + 5;
		byte[] tokens = new byte[length];
		
		// 计算长度
		tokens[0] = (byte) (  xmlLength & 0x000000ff );
		tokens[1] = (byte) (( xmlLength & 0x0000ff00 ) >> 8);
		tokens[2] = (byte) (( xmlLength & 0x00ff0000 ) >> 16);		
		tokens[3] = (byte) (( xmlLength & 0xff000000 ) >> 24);			
		tokens[4] = (byte) ( '0' + times );
		
		System.arraycopy(record.getBytes("UTF-8"), 0, tokens, 5, length - 5);

		tokens[length-2] = '\r';
		tokens[length-1] = '\n';
	
		return tokens;
		
	}
	
	/**
	 * 将生成的消息记录按照内部数据包格式进行转换，并将编码转换为UTF-8格式
	 * @param times    发送次数
	 * @param datatype 数据类型
	 * @param title    文件名
	 * @param xml      XML实体
	 * @return         用于socket通信的字节串
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] produceISCASPackage( int times, String datatype, String title, String xml ) throws UnsupportedEncodingException
	{
		if ( title.length() < 57 )
			for( int i = 0; i < 57; i++ ) title += " ";
		
		if ( datatype.length() > 2 )
			datatype = "FF";
		
		return produceISCASPackage( times, datatype + title + xml + "\r\n" );
	}
}
