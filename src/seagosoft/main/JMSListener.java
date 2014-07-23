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
	private ISCASSocketClient   client;    // ����ͨ������
	private Timer               timer;     // ��ʱ��
	
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
		client    = new ISCASSocketClient();
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
		
		while (flag)
		{
			String str = "";
			
			// ����
			lock.lock();
			
			// ��MQ��������
			try {
				str = connector.recv(10000);
				count_mq++;
			} catch ( JMSException e ) {
				System.out.println( "receive from mq faield >" + str );
				e.printStackTrace();
				continue;
			}
			
			// ���Ӻ�̨������
			try {
				client.connect( servAddrString, servPort );
			} catch ( UnknownHostException e ) {
				System.out.println( "invalid host name >" + servAddrString );
				e.printStackTrace();
				count_lost++;
				break;
			} catch ( IOException e ) {
				System.out.println( "connection to server failed" );
				e.printStackTrace();
				count_lost++;
				break;
			}
				
			// ��������
			try {
				if ( passingInfo )
				{
					if ( !client.send(str, 0, ISCASSocketClient.SG_GENERATE_PASSING_TOKENS) )
					{
						System.out.println( "send failed!" );
						count_lost++;
					}
					else {
						// ���ͳɹ�
						count_send++;
					}
				}	
				else
				{
					if ( !client.send(str, 0, ISCASSocketClient.SG_GENERATE_ILLEGAL_TOKENS) )
					{
						System.out.println( "send failed!" );
						count_lost++;
					}
					else {
						// ���ͳɹ�
						count_send++;						
					}
				}				
			} catch ( IOException e ) {
				System.out.println( "connection between host and client is shutdown" );
				count_lost++;
				e.printStackTrace();
			} catch (UnknownStringException e) {
				System.out.println( "string is invalid" );
				count_mq--;
			} finally {
				// �Ͽ�����
				try {
					client.close();
				} catch (IOException e1) { e1.printStackTrace(); }
				// ����
				lock.unlock();
			}		
		}
		
		if ( lock.isLocked() ) lock.unlock();
	}
}