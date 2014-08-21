package iscas.seagochen.analysis;

import iscas.seagochen.frontend.socket.HisenseMQAnalysisSocket;

import java.io.UnsupportedEncodingException;

import javax.jms.JMSException;

public class MQReportAnalysisMain {
	
	private HisenseMQAnalysisSocket socket;
	
	public MQReportAnalysisMain( String url, String topic, int milliseconds )
	{
		socket = new HisenseMQAnalysisSocket(url, topic, milliseconds);
		
		try { 
			socket.conf();
		} catch (JMSException e) {
			e.printStackTrace();
			System.exit( 1 );
		}
	}
	
	public void recordMessage() {
		try {
			socket.connect();
		} catch (JMSException e) {
			e.printStackTrace();
			System.exit( 1 );
		}
		
		for ( int i = 0; i < 1000; i++ )
		{
			try {
				System.out.println( new String(socket.recv("UTF-8"), "UTF-8") );
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main( String argv[] )
	{
		
	}

}
