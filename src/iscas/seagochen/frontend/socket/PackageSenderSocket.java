package iscas.seagochen.frontend.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import iscas.seagochen.exceptions.ConnectionFailedException;
import iscas.seagochen.exceptions.UnimplementedMethodException;
import iscas.seagochen.exceptions.UnknownExceptionOccurs;
import iscas.seagochen.format.*;;

public class PackageSenderSocket implements FrontEndSocket {
	
	private Socket socket;

	private DataOutputStream output;
	private DataInputStream  input;
	
	private byte[] sendTokens;
	private byte[] recvTokens;
	
	private String servIP;
	private int    servPort;
	private int    milliseconds;
	private int    times;
	
	public PackageSenderSocket( String servIP, int servPort, int milliseconds )
	{
		if ( milliseconds < 0 ) milliseconds = 1000 * 10;
		
		this.milliseconds = milliseconds;
		this.servIP       = servIP;
		this.servPort     = servPort;
	}
	
	public PackageSenderSocket( String servIP, int servPort ) 
	{
		// 默认超时连接为10s
		this.milliseconds = 1000 * 10;
		this.servIP       = servIP;
		this.servPort     = servPort;
	}
	
	public void setSocket( String servIP, int servPort, int milliseconds )
	{
		if ( milliseconds < 0 ) milliseconds = 1000 * 10;
		
		this.milliseconds = milliseconds;
		this.servIP       = servIP;
		this.servPort     = servPort;
	}
	
	@Override
	public void conf() {
//		servIP   = "127.0.0.1";
//		servPort = 123450;
//		milliseconds = 1000 * 10;
	}

	@Override
	public void connect() throws UnknownHostException, IOException  {
		socket = new Socket( servIP, servPort );
		socket.setSoTimeout( milliseconds );
		input  = new DataInputStream( socket.getInputStream() );
		output = new DataOutputStream( socket.getOutputStream() );
	}

	@Override
	public byte[] recv(String code) throws UnimplementedMethodException  {
		throw new UnimplementedMethodException();
	}

	@Override
	public byte[] recv() throws IOException, ConnectionFailedException {
		
		if ( null == socket || null == input ) throw new NullPointerException();
		
		if ( !socket.isConnected() || socket.isClosed() || socket.isInputShutdown() )
			throw new ConnectionFailedException();
		
		recvTokens = new byte[1024];
		
		int recv = input.read(recvTokens);
		
		if ( recv < 0 )
		{
			return recvTokens;
		}
		
		return recvTokens;
	}

	@Override	
	public void send(byte[] pack) throws IOException, ConnectionFailedException {
		
		if ( null == socket || null == output ) throw new NullPointerException();
		
		if ( !socket.isConnected() || socket.isClosed() || socket.isOutputShutdown() )
			throw new ConnectionFailedException();
		
		ProduceISCASPackage producer = new ProduceISCASPackage();
		sendTokens = producer.produceISCASPackage( times, pack );
		
		output.write(sendTokens);
		output.flush();
	}

	@Override
	public void close() throws IOException, UnknownExceptionOccurs {
		input.close();
		output.close();
		socket.close();	
		
		if ( !socket.isInputShutdown() || !socket.isOutputShutdown() )
			throw new UnknownExceptionOccurs( "cannot shutdown I/O" );
		
		if ( !socket.isClosed() )
			throw new UnknownExceptionOccurs( "cannot shutdown socket" );
	}
	
	public void sentTimes( int times ) {
		this.times = times;
	}
	
}
