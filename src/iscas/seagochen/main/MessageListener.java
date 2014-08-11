package iscas.seagochen.main;

import iscas.seagochen.frontend.socket.HisenseMQSocket;
import iscas.seagochen.frontend.socket.PackageSenderSocket;

public class MessageListener implements Listener, Runnable {
	
	private HisenseMQSocket hisenseSocket;
	private PackageSenderSocket senderSocket;
	private int recvmsg; // received message from MQ
	private int sentmsg; // message re-packed and sent to server
	
	public void setHisenseMQ( String url, String topic, int milliseconds )
	{
		// default delay time is 5 seconds 
		if ( milliseconds < 0 ) milliseconds = 1000 * 5;
		
		recvmsg = 0;
		hisenseSocket = new HisenseMQSocket( url, topic, milliseconds );
	}
	
	public void setSender( String servIP, int servPort )
	{
		// default port is 12350
		if ( servPort < 0 ) servPort = 12350; 
		
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
