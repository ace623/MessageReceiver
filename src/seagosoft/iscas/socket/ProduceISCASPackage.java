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
		tokens = new byte[4096];
		String tempBuf = XML + "\r\n";
		byte[] temp = tempBuf.getBytes();
				
		tokens[3] = (byte) ((XML.length() & 0xff000000 ) >> 24);
		tokens[2] = (byte) ((XML.length() & 0x00ff0000 ) >> 16);
		tokens[1] = (byte) ((XML.length() & 0x0000ff00 ) >> 8);
		tokens[0] = (byte) (XML.length() & 0x000000ff );	
		tokens[4] = (byte) sendTimes;
		
		for ( int i = 0; i < temp.length; i++ )
			tokens[i+5] = temp[i];
		
		return tokens;
	}	
}
