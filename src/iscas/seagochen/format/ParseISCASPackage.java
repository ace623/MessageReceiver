package iscas.seagochen.format;

import java.io.UnsupportedEncodingException;

public class ParseISCASPackage {
	private int xmlSize;
	private int sendTimes;
	private String dataType;
	private String title;
	private String xml;
	private String tail;
	
	private int parseSize( byte[] pack )
	{
		long op4 = pack[0];
		long op3 = pack[1];
		long op2 = pack[2];
		long op1 = pack[3];
		
		int length = 0;

		op1 = (op1 << 24) & 0xff000000;
		op2 = (op2 << 16) & 0x00ff0000;
		op3 = (op3 << 8)  & 0x0000ff00;
		op4 = op4 & 0x000000ff;
		
		length = (int) (op1 | op2 | op3 | op4);
		
		return length;
	}
	
	private String parseDatatype( byte[] ISCASPack )
	{
		byte[] temp = new byte[2];
		temp[0] = ISCASPack[5];
		temp[1] = ISCASPack[6];
		
		return new String(temp);
	}
	
	private String parseXMLTitle( byte[] ISCASPack )
	{
		byte[] title = new byte[57];
		
		System.arraycopy(ISCASPack, 7, title, 0, 57);
		
		return new String(title);
	}
	
	private String parseXML( byte[] ISCASPack )
	{		
		byte[] buffer = new byte[xmlSize];
		
		System.arraycopy(ISCASPack, 64, buffer, 0, xmlSize);
		
		String str = "";
		
		try {
			str = new String(buffer, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	private String parseTail( byte[] ISCASPack )
	{
		byte[] packTail = new byte[2];
		
		if ( 64 + xmlSize > ISCASPack.length ) return "";

		packTail[0] = ISCASPack[64 + xmlSize];
		packTail[1] = ISCASPack[65 + xmlSize];
		
		return new String(packTail);
	}
	
	/**
	 * 解析内部数据包
	 * @param ISCASPack
	 */
	public void read( byte[] ISCASPack )
	{
		xmlSize   = parseSize( ISCASPack );
		sendTimes = (int)(ISCASPack[4] - '0');
		dataType  = parseDatatype( ISCASPack ); 
		title     = parseXMLTitle( ISCASPack );
		xml       = parseXML( ISCASPack );
		tail      = parseTail( ISCASPack );
	}
	
	/**
	 * 打印数据包
	 */
	public void print()
	{
		System.out.println( "size: " + xmlSize );
		System.out.println( "send: " + sendTimes );
		System.out.println( "type: " + dataType );
		System.out.println( "name: " + title );
		System.out.println( "data: " + xml );
	}
	

	/**
	 * 检查数据包有效性
	 * @return 数据包正确(0)，无法解析(-1)，数据项长度错误(1)，包尾错误(2)，数据类型错误(3)
	 * @throws UnsupportedEncodingException 
	 */
	public int checkValid() throws UnsupportedEncodingException
	{
		if ( xmlSize != xml.getBytes("UTF-8").length ) return 1;
		if ( ! "\r\n".equals(tail) ) return 2;
		if ( Integer.parseInt(dataType) > 4 || Integer.parseInt(dataType) < 0 ) return 3;
		
		return 0;
	}
}
