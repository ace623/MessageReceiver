package seagosoft.iscas.tester;

import java.util.Properties;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;

public class TestLogger {
	
	private static void configureProp()
	{
		Properties prop = new Properties();
		
		// ����log�ļ�����
		
		// ����root log������Ϣ�ȼ�
		prop.setProperty("log4j.rootLoogger", "DEBUG,rootlog");
		// �������Ŀ�ĵ�
		prop.setProperty("log4j.appender.rootlog", "org.apache.log4j.DailyRollingFileAppender");
		// �������ģ��
		prop.setProperty("log4j.appender.rootlog.layout", "org.apache.log4j.PatternLayout");
		// �����������
		prop.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{HH:mm:ss,SSS} [%t] %-5p %C{1} : %m%n");
		
		// ������Ч
		PropertyConfigurator.configure(prop);
	}
	
	public static void main(String args[])
	{
		configureProp();
		
		Logger logger = Logger.getLogger("MyLogger");
		logger.setLevel(Level.INFO);
		SimpleLayout layout = new SimpleLayout();

		FileAppender appender = null;

		try {

		appender = new FileAppender(layout,"runtime.log",false);

		} catch(Exception e) { e.printStackTrace(); }
		
		logger.addAppender(appender);
		
		

		logger.setLevel((Level) Level.DEBUG);

		logger.debug("Here is some DEBUG");

		logger.info("Here is some INFO");

		logger.warn("Here is some WARN");

		logger.error("Here is some ERROR");

		logger.fatal("Here is some FATAL");
		
		System.out.println("exit...");
	}	
}
