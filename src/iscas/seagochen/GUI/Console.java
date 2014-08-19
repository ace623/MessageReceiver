package iscas.seagochen.GUI;

import java.util.Scanner;

import iscas.seagochen.frontend.socket.HisenseMQSocket;
import iscas.seagochen.main.ApacheMessageListener;

public class Console implements Runnable {
	
	private Scanner scanner;
	private ApacheMessageListener listener;
	private String remoteUrl;
	private String remoteTopic;
	private String servIP;
	private int servPort;
	private String input;
	
	public void open() 
	{
		scanner = new Scanner( System.in );
	}
	
	public void close()
	{
		scanner.close();
	}
	
	@Override
	public void run() {
		
		listener = new ApacheMessageListener(
				remoteUrl, remoteTopic, 1000 * 10, servIP, servPort );
		
		listener.conf();
		
		listener.connect();
		
		while ( listener.getFlag() )
		{
			input = scanner.next();
			
			if ( "exit".equals(input) || "quit".equals(input) )
			{
				listener.setFlag(false);
				break;
			}
		}
		
		listener.disconnect();
	}
	
	public void setParams() 
	{
		String remoteUrlInfo = "";
		String remoteTopicInfo = "";
		
		
		System.out.println( remoteUrlInfo );
		input = scanner.next();
	}
	
	
	
	private static Console console;
	private static Thread thread;
	
	private static void init()
	{
		console = new Console();
		thread  = new Thread( console );
	}
	
	public static void main( String argv[] )
	{
		init();
		
		console.open();
		console.setParams();
		thread.run();		
		console.close();
	}
}
