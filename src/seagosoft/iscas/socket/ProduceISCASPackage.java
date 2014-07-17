package seagosoft.iscas.socket;

public class ProduceISCASPackage {
	
	private byte[] tokens;
	
	/**
	 * 生成内部格式数据包
	 * @param sendTimes  数据包发送次数
	 * @param dataType   数据类型
	 * @param title      XML文件名
	 * @param XML        XML文件实体
	 * @return           byte数组，用于socket通信用
	 */
	public byte[] produceISCASPackage( int sendTimes, String XML )
	{
		String buff = XML + "\r\n";
		int length = buff.getBytes().length + 5;
		int xmlLength = length - 66;
		tokens = new byte[length];
		
		// 计算长度
		tokens[0] = (byte) (  xmlLength & 0x000000ff );
		tokens[1] = (byte) (( xmlLength & 0x0000ff00 ) >> 8);
		tokens[2] = (byte) (( xmlLength & 0x00ff0000 ) >> 16);		
		tokens[3] = (byte) (( xmlLength & 0xff000000 ) >> 24);			
		tokens[4] = (byte) ('0' + sendTimes);
		
		System.arraycopy(buff.getBytes(), 0, tokens, 5, length - 5 );
//		for ( int i = 0; i < temp.length; i++ )
//			tokens[i+5] = temp[i];
//		
//		System.out.println( new String(tokens) );
		
		return tokens;
	}	
}
