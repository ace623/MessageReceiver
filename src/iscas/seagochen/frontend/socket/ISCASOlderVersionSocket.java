package iscas.seagochen.frontend.socket;

import iscas.seagochen.exceptions.ConnectionFailedException;
import iscas.seagochen.exceptions.UnknownStringException;

import java.io.IOException;
import java.net.UnknownHostException;

public class ISCASOlderVersionSocket {
	
	private PackageSenderSocket socket;
	private String servIP;
	private int servPort;
	private int milliseconds;
	
	public ISCASOlderVersionSocket()
	{
		socket = new PackageSenderSocket();
	};
	
	public ISCASOlderVersionSocket( String servIP, int servPort, int milliseconds )
	{
		socket = new PackageSenderSocket(servIP, servPort, milliseconds); 
		setSocket( servIP, servPort, milliseconds );
		
		if ( null == socket )
			throw new NullPointerException();
	}
	
	public void setSocket( String servIP, int servPort, int milliseconds )
	{
		this.servIP = servIP;
		this.servPort = servPort;
		this.milliseconds = milliseconds;
	}
	
	public void send(int times, byte[] bytes)
			throws UnknownHostException, IOException,
			ConnectionFailedException, UnknownStringException
	{
		socket.setSocket(servIP, servPort, milliseconds);
		socket.connect();
		socket.times(times);
		socket.send(bytes);
	}
	
	public byte[] recv() throws IOException, ConnectionFailedException {
		return socket.recv();
	}

	public void close() throws IOException {
		socket.close();
	}
}
