package iscas.seagochen.analysis;

import iscas.seagochen.frontend.socket.HisenseMQAnalysisSocket;
import iscas.seagochen.frontend.socket.HisenseMQSocket;

import java.awt.print.Printable;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.jms.JMSException;

import org.omg.CORBA.PUBLIC_MEMBER;

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
		
		PrintWriter out = null;
		File f = new File("e:\\record.txt");
		
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(f,true)));
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		for( int i = 0; i < 1000; i++ )
		{
			long currentTime=System.currentTimeMillis();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(new Date(currentTime));
			try {
				out.println(date + "\t" + new String(socket.recv("UTF-8"), "UTF-8") );
				System.out.println(date + "\t" + new String(socket.recv("UTF-8"), "UTF-8") );
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		out.close();
	}
	
	public static void main( String argv[] )
	{
		MQReportAnalysisMain analysis = new MQReportAnalysisMain(
				HisenseMQSocket.REMOTE_URL1, HisenseMQSocket.HISENSE_PASSING, 10 * 1000);
		
		analysis.recordMessage();
	}

}
