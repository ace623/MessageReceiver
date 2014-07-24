package seagosoft.main;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.locks.ReentrantLock;

import javax.jms.JMSException;

import seagosoft.iscas.exception.UnknownStringException;
import seagosoft.iscas.tools.Timer;

class JMSListener implements Runnable
{
	// fixed URL
	private String url1 = "tcp://52.1.101.193:61616";
	private String url2 = "tcp://52.1.101.195:61616";
	private String url3 = "tcp://10.161.14.6:61616";
	
	// fixed topic
	private String passingTopic = "HIATMP.HISENSE.PASS.PASSINF";
	private String illegalTopic = "HIATMP.HISENSE.ILLEGAL";
	
	// �Զ�����������
	private ApacheMQConnector   connector; // MQ ��������
	private Timer               timer;     // ��ʱ��
	private ISCASSocketClient   client;    // ����ͨ������
	
	// socket
	private String servAddrString;
	private int servPort;
	
	// �߳���
	private ReentrantLock lock;
	
	// flag
	private boolean flag;
	
	// ������
	private int count_mq;
	private int count_lost;
	private int count_send;	
	
	// ���ݰ�����
	private boolean passingInfo; // true-�������ݣ� 1-Υ������
	
	public JMSListener()
	{
		flag = true;
		count_lost = count_mq = count_send = 0;
	}
	
	public boolean checkInput( String argv[] )
	{
		//<usage> url_no info_type back_addr port
		
		if ( argv.length < 4 ) return false;
		
		int select = 0;
		if ( "url1".equals(argv[0]) ) select = 1;
		if ( "url2".equals(argv[0]) ) select = 2;
		if ( "url3".equals(argv[0]) ) select = 3;
		if ( select == 0 )
		{
			System.out.println( "1st parameter error, use \"url1\", \"url2\", \"url3\"" );
			return false;
		}
		
		String topic = "";
	
		if ( "passing".equals(argv[1]) ) 
		{
			topic = passingTopic;
			passingInfo = true;
		}
		
		if ( "illegal".equals(argv[1]) ) 
		{
			topic = illegalTopic;
			passingInfo = false;
		}
		if ( "".equals(topic) )
		{
			System.err.println( "2nd parameter error, use \"passing\", \"illegal\"" );
			return false;
		}
		
		servAddrString = argv[2];
		servPort = Integer.parseInt(argv[3]);
		
		try {
			init(topic, select);
		} catch (JMSException exception) {
			exception.printStackTrace();
			System.err.println( "init failed" );
			return false;
		}
		
		return true;
	}
	
	private void init( String apacheTopic, int select ) throws JMSException
	{
		connector = new ApacheMQConnector();
		timer     = new Timer();
		lock      = new ReentrantLock();
		
		switch (select)
		{
		case 1:
			connector.connect(url1, apacheTopic);
			break;
		case 2:
			connector.connect(url2, apacheTopic);
			break;
		case 3:
			connector.connect(url3, apacheTopic);
			break;

		default: 
			break;
		}
	}
	
	public void setFlag( boolean flag )
	{
		this.flag = flag;
	}
	
	public void setCount( int mq, int lost, int send )
	{
		count_mq = mq;
		count_send = send;
		count_lost = lost;
	}
	
	public int getCountOfMQ() { return count_mq; }
	
	public int getCountOfSend() { return count_send; }
	
	public int getCountOfLost() { return count_lost; }
	
	public void run()
	{
		timer.resetTimer();
		timer.setTimeout(5000); // 5 seconds
		
		ClientThreads clientThread = null;
		while (flag)
		{
			lock.lock();
			
			String str = "";
			
			// ��MQ��������
			try {
				str = connector.recv(10000);
				count_mq++;
			} catch ( JMSException e ) {
				e.printStackTrace();
				continue;
			}
			
			// ���Ӻ�̨������������������
			clientThread = new ClientThreads(str, servAddrString, servPort);
			//clientThread.start();
			
			if ( !clientThread.connectServ() )
			{
				count_lost++;
				clientThread.closeConnection();
				continue;
			}
			if ( !clientThread.sendToServ() )
			{
				count_lost++;
				clientThread.closeConnection();
				continue;
			}
//			if ( !clientThread.recvFromServ() ) 
//			{
//				count_lost++;
//				clientThread.closeConnection();
//				continue;
//			}
			
			clientThread.closeConnection();
			count_send++;
			
			if ( lock.isLocked() ) lock.unlock();
		}
		
		if ( lock.isLocked() ) lock.unlock();
	}

	class ClientThreads extends Thread
	{
		private String record;
		
		private String servAddr;
		private int servPort;
		
		public ClientThreads( String record, String servAddr, int servPort )
		{
			this.record = record;
			this.servAddr = servAddr;
			this.servPort = servPort;
			
			client = new ISCASSocketClient();
		}
		
		public boolean connectServ()
		{
			try {
				
				client.connect( servAddr, servPort, 30000 );
				return true;
				
			} catch ( UnknownHostException e ) {
				e.printStackTrace();
				return false;
			} catch ( IOException e ) {
				e.printStackTrace();
				return false;
			}
		}

		public boolean sendToServ()
		{
			// ��������
			try {
				if ( passingInfo )
				{
					if ( !client.send(record, 0,
							ISCASSocketClient.SG_GENERATE_PASSING_TOKENS) )
						//System.out.println( "send failed!" );
						return false;
					else
						return true;
				}	
				else
				{
					if ( !client.send(record, 0,
							ISCASSocketClient.SG_GENERATE_ILLEGAL_TOKENS) )
						return false;
					else 
						return true;
				}
			} catch ( IOException e ) {
				
				System.out.println( "connection between host and client is shutdown" );
				e.printStackTrace();
				return false;
				
			} catch (UnknownStringException e) {
				
				System.out.println( "string is invalid" );
				return true;
			} 
		}
		
		public boolean recvFromServ()
		{
			try {
				
				String str = client.recv();
				
				if ( "".equals(str) ) return false;
				else return true;
				
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		
		public boolean closeConnection()
		{
			// �Ͽ�����
			try { 
				client.close();
				return true;
				
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;	
			}
		}
		
		public void run() {
			
			if ( !connectServ() )
			{
				try { client.close(); } 
				catch (IOException e1) { e1.printStackTrace(); }
				return ;
			}
			
			if ( !sendToServ() )
			{
				try { client.close(); }
				catch (IOException e1) { e1.printStackTrace(); }
				return ;
			}
			
			if ( !recvFromServ() )
			{	try { client.close(); }
				catch (IOException e1) { e1.printStackTrace(); }
				return ;
			}
		}
	}
}