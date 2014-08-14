package iscas.seagochen.exceptions;

public class DeviceNotReadyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4040568099606630784L;

	public DeviceNotReadyException()
	{
		super();
	}
	
	public DeviceNotReadyException( String msg )
	{
		super(msg);
	}
	
	public DeviceNotReadyException( String msg, Throwable cause )
	{
		super(msg, cause);
	}
	
	public DeviceNotReadyException(Throwable cause)
	{
		super(cause);
	}
}
