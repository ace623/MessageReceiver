package iscas.seagochen.main;

import iscas.seagochen.exceptions.ConnectionFailedException;
import iscas.seagochen.exceptions.DeviceNotReadyException;
import iscas.seagochen.exceptions.UnknownStringException;
import iscas.seagochen.frontend.socket.HisenseMQSocket;
import iscas.seagochen.frontend.socket.ISCASOlderVersionSocket;
import iscas.seagochen.toolbox.Timer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.jms.JMSException;

public class ApacheMessageListener implements ListenerInterface, Runnable {
	
	private HisenseMQSocket hisenseSocket;
	private ISCASOlderVersionSocket olderSocket;

	private long amountOfIncome, amountOfOutput;
	private ListenerInterface.RunningStatus status;
	public boolean flag;
	
	private Thread thread;
	
	private void init()
	{
		amountOfIncome = amountOfOutput = 0;
		setFlag( false );
		setStatus( RunningStatus.NO_CONNECTION );
	};	
	
	public ApacheMessageListener()
	{
		init();
	}
	
	public ApacheMessageListener( String url, String topic, int milliseconds, String servIP, int servPort )
	{		
		init();
		
		if ( milliseconds < 0 ) 
			milliseconds = 1000 * 5;
		
		if ( servPort < 0 ) 
			servPort = 12350; 
		
		hisenseSocket = new HisenseMQSocket( url, topic, milliseconds );
		olderSocket   = new ISCASOlderVersionSocket( servIP, servPort, milliseconds );
	}
	
	@Override
	public void conf() {
		if ( hisenseSocket == null  || olderSocket == null )
			throw new NullPointerException( "cannot configure uninitialized variables" );
		
		try { 
			hisenseSocket.conf();
		} catch (JMSException e) {
			throw new IllegalArgumentException();
		}		
	}

	@Override
	public void connect() {
		if ( hisenseSocket == null  || olderSocket == null ) 
			throw new NullPointerException( "cannot use uninitialized variables" );
		
		try {
			hisenseSocket.connect();
		} catch (JMSException e) {
			setStatus( RunningStatus.CONNECT_REMOTE_DEVICE_FAILED );
			e.printStackTrace();
			System.exit(1);
		}
		
		setStatus( RunningStatus.CONNECTION_SUCCESS );
		
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void disconnect()  {
		if ( hisenseSocket == null  || olderSocket == null ) 
			throw new NullPointerException( "cannot stop uninitialized variables" );
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			hisenseSocket.close();
			olderSocket.close();
		} catch (JMSException e) {
			status = RunningStatus.CONNECT_REMOTE_DEVICE_FAILED;
			e.printStackTrace();
		} catch (IOException e) {
			status = RunningStatus.CONNECT_REMOTE_DEVICE_FAILED;
			e.printStackTrace();
		}
		
		setStatus( RunningStatus.NO_CONNECTION );
	}
	
	@Override
	public RunningStatus getStatus() { return status; }
	
	@Override
	public void setStatus(RunningStatus status) { this.status = status; }
	
	@Override
	public void setFlag(boolean flag) { this.flag = flag; }
	
	@Override
	public boolean getFlag() { return flag; }

	@Override
	public void run() {
		setFlag( true );
		
		// initialize timeout and set duration as 5s
		Timer timer = new Timer();
		timer.setTimeout( 1000 * 15 ); 
	
		// change current running status
		if ( status == RunningStatus.CONNECTION_SUCCESS )
			setStatus( RunningStatus.RUNNING_STATUS_OK );
		else {
			try {
				throw new DeviceNotReadyException();
			} catch ( DeviceNotReadyException e ) {
				e.printStackTrace();
				System.exit( 1 );
			}
		}
		
		// permanently until someone interrupts looping
		byte[] bytes;
		while( flag )
		{
			try { 
				// trying to receive record
				bytes = hisenseSocket.recv( "UTF-8" );
				
				if ( bytes.length < 60 ) continue;
				
				// received a new record from MQ
				amountOfIncome++;
				
				// trying to send message to server
				olderSocket.send(0, bytes);
				
				// sent a new record to server
				amountOfOutput++;
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.exit( 1 );
			} catch (JMSException e) {
				e.printStackTrace();
			} catch (UnknownStringException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ConnectionFailedException e) {
				e.printStackTrace();
			}
			
			autoFreshStatus(timer);
		}
		
		disconnect();
	}
	
	private void autoFreshStatus( Timer timer ) {
		if ( timer.isTimeout() )
		{
			System.out.println( "amount of income: " + amountOfIncome + 
					"\namount of output: " + amountOfOutput );
			System.out.println( "current : " + timer.getFormattedCurrent() );
			System.out.println( "duration: " + timer.getFormattedDuration() );
			System.out.println( "----------------------------------" );
		}
	}
}
