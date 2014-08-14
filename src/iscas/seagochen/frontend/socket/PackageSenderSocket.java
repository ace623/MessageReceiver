package iscas.seagochen.frontend.socket;

import iscas.seagochen.exceptions.ConnectionFailedException;
import iscas.seagochen.exceptions.UnimplementedMethodException;
import iscas.seagochen.exceptions.UnknownStringException;
import iscas.seagochen.format.ProduceISCASPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

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
	
	public PackageSenderSocket()
	{
		this.milliseconds = this.servPort = 0;
		this.servIP = "0.0.0.0";
	}
	
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
		try {
			throw new UnimplementedMethodException("conf is not implemented");
		} catch (UnimplementedMethodException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connect() throws UnknownHostException, IOException  {		
		socket = new Socket( servIP, servPort );
		input  = new DataInputStream( socket.getInputStream() );
		output = new DataOutputStream( socket.getOutputStream() );
		
		socket.setSoTimeout( milliseconds );
	}

	@Override
	public byte[] recv(String code) throws UnimplementedMethodException {
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
	public void send(byte[] pack) throws IOException, ConnectionFailedException, UnknownStringException {
		
		if ( null == socket || null == output ) throw new NullPointerException();
		
		if ( !socket.isConnected() || socket.isClosed() || socket.isOutputShutdown() )
			throw new ConnectionFailedException();
		
		ProduceISCASPackage producer = new ProduceISCASPackage();
		sendTokens = producer.produceISCASPackage( times, pack );
		
		if ( sendTokens.length < 60 )
			throw new UnknownStringException("token length is less than 60");
		
		output.write(sendTokens);
		output.flush();
	}

	@Override
	public void close() throws IOException {
		input.close();
		output.close();
		socket.close();	
	}
	
	public void times( int times ) {
		this.times = times;
	}
	
}
