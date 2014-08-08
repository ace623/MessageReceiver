package iscas.seagochen.frontend.socket;

public abstract class FrontEndSocket {
	
	/**
	 * ����socket
	 */
	public abstract void conf() throws Exception;
	
	/**
	 * ����Զ���豸
	 */
	public abstract void connect() throws Exception;
	
	/**
	 * ��Զ���豸��������
	 * @param code �����յ�������ת��Ϊָ�������ʽ����UTF-8��GBK
	 * @return �յ����ֽڴ�
	 */
	public abstract byte[] recv(String code) throws Exception;
	
	/**
	 * ��Զ���豸��������
	 * @return �յ����ֽڴ�
	 */
	public abstract byte[] recv() throws Exception;
	
	/**
	 * ����������Զ���豸
	 * @param pack ��Ҫ���͵�����
	 */
	public abstract void send(byte[] pack) throws Exception;
	
	/**
	 * �ر�����
	 */
	public abstract void close() throws Exception;
}
