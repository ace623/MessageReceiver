package iscas.seagochen.format;

import java.io.UnsupportedEncodingException;

public class ProduceISCASPackage {
	
	/**
	 * �����ɵ���Ϣ��¼�����ڲ����ݰ���ʽ����ת������������ת��ΪUTF-8��ʽ
	 * @param times  ���ݷ��ʹ���
	 * @param record ����MQ����Ϊ���������Ѱ����������͡����⡢XML���ݡ����ݰ�β\r\n
	 * @return       ����socket�ֽڴ�
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] produceISCASPackage( int times, String record ) throws UnsupportedEncodingException
	{	
		int xmlLength = record.getBytes("UTF-8").length - 61;
		int length = record.getBytes("UTF-8").length + 5;
		byte[] tokens = new byte[length];
		
		// ���㳤��
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
	 * �����ɵ���Ϣ��¼�����ڲ����ݰ���ʽ����ת������������ת��ΪUTF-8��ʽ
	 * @param times    ���ʹ���
	 * @param datatype ��������
	 * @param title    �ļ���
	 * @param xml      XMLʵ��
	 * @return         ����socketͨ�ŵ��ֽڴ�
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
