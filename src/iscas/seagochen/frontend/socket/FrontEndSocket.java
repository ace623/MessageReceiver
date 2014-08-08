package iscas.seagochen.frontend.socket;

public abstract class FrontEndSocket {
	
	/**
	 * 配置socket
	 */
	public abstract void conf() throws Exception;
	
	/**
	 * 连接远程设备
	 */
	public abstract void connect() throws Exception;
	
	/**
	 * 从远程设备接收数据
	 * @param code 将接收到的数据转换为指定编码格式，如UTF-8，GBK
	 * @return 收到的字节串
	 */
	public abstract byte[] recv(String code) throws Exception;
	
	/**
	 * 从远程设备接收数据
	 * @return 收到的字节串
	 */
	public abstract byte[] recv() throws Exception;
	
	/**
	 * 发送数据至远程设备
	 * @param pack 需要发送的数据
	 */
	public abstract void send(byte[] pack) throws Exception;
	
	/**
	 * 关闭连接
	 */
	public abstract void close() throws Exception;
}
