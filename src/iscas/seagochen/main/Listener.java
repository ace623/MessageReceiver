package iscas.seagochen.main;

public interface Listener {
	
	public static enum RunningStatus {
		/**
		 * 每5s检测一次数据接收情况，若一直能接收到数据则返回该状态
		 */
		RUNNING_STATUS_OK, 
		
		/**
		 * 与远程设备的连接已经建立
		 */
		CONNECTION_SUCCESS,
		
		/**
		 * 检测当前数据接收状况时，发现与上次统计情况相同，则返回该状态
		 */
		NO_INCOMMING_MESSAGE,
		
		/**
		 * 连续5次发现统计情况相同，怀疑连接中断或远程设备出现错误，切换连接
		 */
		CONNECTION_UNKNOWN_ERROR,
		
		/**
		 * 不能建立与远程设备的连接
		 */
		CONNECT_REMOTE_DEVICE_FAILED,
		
		/**
		 * 不能关闭与远程设备的连接
		 */
		CANNOT_SHUTDOWN_CONNECTION,
		
		/**
		 * 当前的连接状态为关闭
		 */
		NO_CONNECTION,
	};
	
	/**
	 * 系统配置
	 */
	public void conf();

	/**
	 * 启动连接，监听消息
	 */
	public void connect();

	/**
	 * 断开连接，不在接收消息
	 */
	public void disconnect();
	
	/**
	 * 设置运行状态
	 * @param flag
	 */
	public void setFlag(boolean flag);
	
	/**
	 * 运行参数
	 * @return
	 */
	public boolean getFlag();
	
	/**
	 * 检查运行状态
	 * @return
	 */
	public RunningStatus getStatus();
	
	/**
	 * 设置运行状态
	 * @param status
	 */
	public void setStatus(RunningStatus status);
}
