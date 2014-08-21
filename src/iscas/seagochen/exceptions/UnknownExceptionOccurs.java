package iscas.seagochen.exceptions;

public class UnknownExceptionOccurs extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 158408813437253225L;

	public UnknownExceptionOccurs()
	{
		super();
	}
	
	public UnknownExceptionOccurs( String msg )
	{
		super(msg);
	}
	
	public UnknownExceptionOccurs( String msg, Throwable cause )
	{
		super(msg, cause);
	}
	
	public UnknownExceptionOccurs(Throwable cause)
	{
		super(cause);
	}
}
