package seagosoft.iscas.tester;

import java.io.*; 
import java.net.*; 

class CreateServerThread extends Thread
{
	private Socket client;
	private BufferedReader in;
	private PrintWriter out;
	
	public CreateServerThread(Socket s) throws IOException
	{
		client = s;
		
		in = new BufferedReader(new InputStreamReader(client.getInputStream(), "GB2312"));
		out = new PrintWriter(client.getOutputStream(), true);
		out.println("--- Welcome ---");
		start();
	}
	
	public void run()
	{
		try
		{
			String line = in.readLine();
			while ( !"bye".equals(line) )
			{
				System.out.println( line );
				//out.println(msg);
				
				line = in.readLine();
			}
		
			out.println("--- See you, bye! ---");
			
			client.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}	
	
	}
	
	
	private String createMessage(String line)
	{
		return line;		
	}
	
} 

public class SocketServerMultiThreads extends ServerSocket 
{
	private static final int SERVER_PORT = 10000;
	
	public SocketServerMultiThreads() throws IOException
	{
		super(SERVER_PORT);
		
		try
		{	
			while (true)
			{
				Socket socket = accept();
				new CreateServerThread(socket);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			close();
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		new SocketServerMultiThreads();
	} 
} 