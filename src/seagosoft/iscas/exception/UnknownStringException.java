package seagosoft.iscas.exception;

public class UnknownStringException extends Exception
{
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