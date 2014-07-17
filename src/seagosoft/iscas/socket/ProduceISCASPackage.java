package seagosoft.iscas.socket;

import java.io.UnsupportedEncodingException;

public class ProduceISCASPackage {
	
	private byte[] tokens;
	
	/**
	 * �����ڲ���ʽ���ݰ�
	 * @param sendTimes  ���ݰ����ʹ���
	 * @param dataType   ��������
	 * @param title      XML�ļ���
	 * @param XML        XML�ļ�ʵ��
	 * @return           byte���飬����socketͨ����
	 * @throws UnsupportedEncodingException 
	 */
	public byte[] produceISCASPackage( int sendTimes, String XML )
	{
//		System.out.println(XML);
		String buff = XML + "\r\n";
		int length;

//		System.out.println(XML.getBytes().length);
//		System.out.println(buff.getBytes().length);
		length = buff.getBytes().length + 5;
		int xmlLength = length - 66;
		tokens = new byte[length];
		
		// ���㳤��
		tokens[0] = (byte) (  xmlLength & 0x000000ff );
		tokens[1] = (byte) (( xmlLength & 0x0000ff00 ) >> 8);
		tokens[2] = (byte) (( xmlLength & 0x00ff0000 ) >> 16);		
		tokens[3] = (byte) (( xmlLength & 0xff000000 ) >> 24);			
		tokens[4] = (byte) ( '0' + sendTimes );
		
		System.arraycopy(buff.getBytes(), 0, tokens, 5, length - 5 );
			
//		for ( int i = 0; i < temp.length; i++ )
//			tokens[i+5] = temp[i];
//		
//		System.out.println( new String(tokens) );
//		
//		//return tokens = new String(tokens).getBytes("utf-8");
		return tokens;
	}	
}
