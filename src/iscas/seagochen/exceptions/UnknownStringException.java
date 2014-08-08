package iscas.seagochen.exceptions;

public class UnknownStringException extends Exception
{
	private static final long serialVersionUID = -896858467375705400L;

	public UnknownStringException()
	{
		super();
	}
	
	public UnknownStringException( String msg )
	{
		super(msg);
	}
	
	public UnknownStringException( String msg, Throwable cause )
	{
		super(msg, cause);
	}
	
	public UnknownStringException(Throwable cause)
	{
		super(cause);
	}
}