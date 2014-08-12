package iscas.seagochen.exceptions;

public class ConnectionFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6564287572664325604L;
	
	public ConnectionFailedException()
	{
		super();
	}
	
	public ConnectionFailedException( String msg )
	{
		super(msg);
	}
	
	public ConnectionFailedException( String msg, Throwable cause )
	{
		super(msg, cause);
	}
	
	public ConnectionFailedException(Throwable cause)
	{
		super(cause);
	}


}
