package iscas.seagochen.main;

public interface Listener {
	
	public static enum RunningStatus {
		/**
		 * ÿ5s���һ�����ݽ����������һֱ�ܽ��յ������򷵻ظ�״̬
		 */
		RUNNING_STATUS_OK, 
		
		/**
		 * ��Զ���豸�������Ѿ�����
		 */
		CONNECTION_SUCCESS,
		
		/**
		 * ��⵱ǰ���ݽ���״��ʱ���������ϴ�ͳ�������ͬ���򷵻ظ�״̬
		 */
		NO_INCOMMING_MESSAGE,
		
		/**
		 * ����5�η���ͳ�������ͬ�����������жϻ�Զ���豸���ִ����л�����
		 */
		CONNECTION_UNKNOWN_ERROR,
		
		/**
		 * ���ܽ�����Զ���豸������
		 */
		CONNECT_REMOTE_DEVICE_FAILED,
		
		/**
		 * ���ܹر���Զ���豸������
		 */
		CANNOT_SHUTDOWN_CONNECTION,
		
		/**
		 * ��ǰ������״̬Ϊ�ر�
		 */
		NO_CONNECTION,
	};
	
	/**
	 * ϵͳ����
	 */
	public void conf();

	/**
	 * �������ӣ�������Ϣ
	 */
	public void connect();

	/**
	 * �Ͽ����ӣ����ڽ�����Ϣ
	 */
	public void disconnect();
	
	/**
	 * ��������״̬
	 * @param flag
	 */
	public void setFlag(boolean flag);
	
	/**
	 * ���в���
	 * @return
	 */
	public boolean getFlag();
	
	/**
	 * �������״̬
	 * @return
	 */
	public RunningStatus getStatus();
	
	/**
	 * ��������״̬
	 * @param status
	 */
	public void setStatus(RunningStatus status);
}
