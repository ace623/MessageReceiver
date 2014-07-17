package seagosoft.iscas.socket;

public class ParseISCASPackage {
	private int xmlSize;
	private int sendTimes;
	private String dataType;
	private String title;
	private String xml;
	
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
		if ( xmlSize < 0 || xmlSize > 9999999) return "";
		
		byte[] buf = new byte[4096];
		
		System.arraycopy(ISCASPack, 64, buf, 0, xmlSize);
		
		return new String(buf);
	}
	
	public void read( byte[] ISCASPack )
	{
		xmlSize   = parseSize( ISCASPack );
		sendTimes = (int)(ISCASPack[4] - '0');
		dataType  = parseDatatype( ISCASPack ); 
		title     = parseXMLTitle( ISCASPack );
		xml       = parseXML( ISCASPack );
	}
	
	public void print()
	{
		System.out.println( "size: " + xmlSize );
		System.out.println( "send: " + sendTimes );
		System.out.println( "type: " + dataType );
		System.out.println( "name: " + title );
		System.out.println( "data: " + xml );
	}
}
