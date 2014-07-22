package seagosoft.main;

import java.util.Scanner;

public class HisenseMQListener implements Runnable
{		
	// 收发数据
	private static JMSListener  listener;
	
	// 子线程
	private static Thread thread;
	
	public static void main( String[] argv )
	{
		 listener = new JMSListener();
		
		if ( ! listener.checkInput(argv) )
		{
			System.err.println( "invalid parameters, check your input!" );
			System.err.println( "<usage> url_no info_type back_addr port" );
			System.exit(0);
		}
		
		thread = new Thread(listener);
		thread.start();
		
		Thread console = new Thread( new HisenseMQListener() );
		console.setDaemon(true);
		console.start();
		
		try {
			console.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.exit( 0 );
	}
	
	public void run()
	{
		System.out.println("JMS is running!");
		
		// 监听输入
		Scanner scanner = new Scanner(System.in);
		String command = null;
		
		while ( true )
		{
			System.out.print("prompt>");
			command = scanner.nextLine();
			
			if ( "exit".equals(command) ) break;
			if ( "print".equals(command) )
			{
				System.out.println( "pull from mq: " + listener.getCountOfMQ() );
				System.out.println( "lost package: " + listener.getCountOfLost() );
				System.out.println( "package sent: " + listener.getCountOfSend() );
				continue;
			}
			if ( "help".equals(command) )
			{
				System.out.println("print ------ print the count of packages out");
				System.out.println("exit  ------ exit message receiver");
				continue;
			}			
		}
		
		scanner.close();
		
		listener.setFlag(false);
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println( "goodbye!" );		
	}
}