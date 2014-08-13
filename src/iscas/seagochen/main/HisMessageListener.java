package iscas.seagochen.main;

import java.io.IOException;
import java.net.UnknownHostException;

import iscas.seagochen.exceptions.ConnectionFailedException;
import iscas.seagochen.exceptions.UnknownExceptionOccurs;
import iscas.seagochen.exceptions.UnknownStringException;
import iscas.seagochen.frontend.socket.HisenseMQSocket;
import iscas.seagochen.frontend.socket.PackageSenderSocket;
import iscas.seagochen.toolbox.Timer;

import javax.jms.JMSException;

public class HisMessageListener implements Listener {
	
	private HisenseMQSocket hisenseSocket;
	private PackageSenderSocket senderSocket;
	
	/**
	 * the number of message received from Apache MQ
	 */
	private long recvMsgNew, recvMsgPre;
	
	/**
	 * the number of message sent to back-server
	 */
	private long sentMsgNew, sentMsgPre;
	
	/**
	 * running status
	 */
	private Listener.RunningStatus status;
	
	/**
	 * running flag
	 */
	public boolean flag;
	
	private Thread thread;
	
	public HisMessageListener()
	{
		recvMsgNew = recvMsgPre = 0;
		sentMsgNew = sentMsgPre = 0;
		
		flag = false;
	}
	
	public HisMessageListener( String url, String topic, int milliseconds, String servIP, int servPort )
	{
		setHisenseMQ(url, topic, milliseconds);
		setSender(servIP, servPort);
		
		recvMsgNew = recvMsgPre = 0;
		sentMsgNew = sentMsgPre = 0;
		
		flag = false;
	}
	
	public void setHisenseMQ( String url, String topic, int milliseconds )
	{
		// default delay time is 5 seconds 
		if ( milliseconds < 0 ) milliseconds = 1000 * 5;
		
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
		
		try {
			
			hisenseSocket.conf();
			senderSocket.conf();
			
		} catch (JMSException e) {
			throw new IllegalArgumentException();
		}
		
		status = RunningStatus.NO_CONNECTION;
	}

	@Override
	public void start() {
		if ( hisenseSocket == null  || senderSocket == null ) 
			throw new NullPointerException( "cannot start uninitialized variables" );
		
		try {
			senderSocket.connect();
			hisenseSocket.connect();
		} catch (JMSException e) {
			status = RunningStatus.CONNECT_REMOTE_DEVICE_FAILED;
			e.printStackTrace();
		} catch (UnknownHostException e) {
			status = RunningStatus.CONNECT_REMOTE_DEVICE_FAILED;
			e.printStackTrace();
		} catch (IOException e) {
			status = RunningStatus.CONNECT_REMOTE_DEVICE_FAILED;
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		if ( hisenseSocket == null  || senderSocket == null ) 
			throw new NullPointerException( "cannot stop uninitialized variables" );
		
		flag = false;
		
		try {
			if ( thread != null )
				thread.join();
		} catch (InterruptedException e) {
			System.err.println( "interruption failed" );
			e.printStackTrace();
		}
		
		try {
			hisenseSocket.close();
			senderSocket.close();
		} catch (JMSException e) {
			status = RunningStatus.CONNECT_REMOTE_DEVICE_FAILED;
			e.printStackTrace();
		} catch (IOException e) {
			status = RunningStatus.CONNECT_REMOTE_DEVICE_FAILED;
			e.printStackTrace();
		}
	}

	@Override
	public RunningStatus status() {
		if ( hisenseSocket == null  || senderSocket == null ) 
			throw new NullPointerException( "cannot check uninitialized variables" );
		
		return status;
	}

	public void run() {
		Timer timer = new Timer();
		timer.setTimeout( 1000 * 5 ); // set timeout 5s
		
		recvMsgNew = recvMsgPre = 0;
		sentMsgNew = sentMsgPre = 0;
		
		timer.resetTimer(); // reset timer
		
		byte[] bytes;
		while( true )
		{
			try {
				bytes = hisenseSocket.recv("UTF-8");
				if ( bytes.length <= 0 ) continue;
				senderSocket.send(bytes);
				
				if ( bytes.length > 0 ) recvMsgNew++;
				
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (UnknownStringException e) {
				e.printStackTrace();
			} catch (ConnectionFailedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			bytes = null;
			System.gc();
			if ( timer.isTimeout() )
			{
				if ( recvMsgNew > recvMsgPre & sentMsgNew > sentMsgPre )
				{
					recvMsgPre = recvMsgNew;
					status = RunningStatus.RUNNING_STATUS_OK; 
				}
			}
		}
	}
	
	public static void main( String argv[] )
	{
		HisMessageListener listener = new HisMessageListener(
				HisenseMQSocket.REMOTE_URL2,
				HisenseMQSocket.HISENSE_PASSING,
				1000 * 10,
				"52.1.126.70",
				12350 );
		
		listener.conf();
		listener.start();
		listener.run();		
		listener.stop();
	}
}
