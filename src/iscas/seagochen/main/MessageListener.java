package iscas.seagochen.main;

import iscas.seagochen.frontend.socket.HisenseMQSocket;
import iscas.seagochen.frontend.socket.PackageSenderSocket;

public class MessageListener implements Listener, Runnable {
	
	private HisenseMQSocket hisenseSocket;
	private PackageSenderSocket senderSocket;
	
	public void setHisenseMQ( String url, String topic, int milliseconds )
	{
		hisenseSocket = new HisenseMQSocket( url, topic, milliseconds );
	}
	
	public void setSender( String servIP, int servPort )
	{
		senderSocket = new PackageSenderSocket( servIP, servPort );
	}
	
	@Override
	public void conf() {
		if ( hisenseSocket == null  || senderSocket == null )
			throw new NullPointerException( "cannot configure uninitialized variables" );
		
		
	}

	@Override
	public void start() {
		if ( hisenseSocket == null  || senderSocket == null ) 
			throw new NullPointerException( "cannot start uninitialized variables" );
		
	}

	@Override
	public void stop() {
		if ( hisenseSocket == null  || senderSocket == null ) 
			throw new NullPointerException( "cannot stop uninitialized variables" );
		
	}

	@Override
	public boolean status() {
		if ( hisenseSocket == null  || senderSocket == null ) 
			throw new NullPointerException( "cannot check uninitialized variables" );
		
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main( String argv[] )
	{
		
	}
}
