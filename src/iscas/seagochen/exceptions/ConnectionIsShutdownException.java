package iscas.seagochen.exceptions;

public class ConnectionIsShutdownException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2032287040487886844L;

	public ConnectionIsShutdownException()
	{
		super();
	}
	
	public ConnectionIsShutdownException( String msg )
	{
		super(msg);
	}
	
	public ConnectionIsShutdownException( String msg, Throwable cause )
	{
		super(msg, cause);
	}
	
	public ConnectionIsShutdownException(Throwable cause)
	{
		super(cause);
	}
}
