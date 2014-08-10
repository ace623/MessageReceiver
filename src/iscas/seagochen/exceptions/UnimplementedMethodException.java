package iscas.seagochen.exceptions;

public class UnimplementedMethodException extends Exception {

	private static final long serialVersionUID = 3683984149818252502L;
	
	public UnimplementedMethodException()
	{
		super();
	}
	
	public UnimplementedMethodException( String msg )
	{
		super(msg);
	}
	
	public UnimplementedMethodException( String msg, Throwable cause )
	{
		super(msg, cause);
	}
	
	public UnimplementedMethodException(Throwable cause)
	{
		super(cause);
	}

}
