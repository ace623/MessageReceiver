package seagosoft.iscas.tester;

import java.util.Properties;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;

public class Testlogger {
	
	public static void main(String args[])
	{
		Properties prop = new Properties();

		prop.setProperty("log4j.rootLogger", "DEBUG, CONSOLE");
		prop.setProperty("log4j.appender.CONSOLE", "org.apache.log4j.ConsoleAppender");
		prop.setProperty("log4j.appender.CONSOLE.layout", "org.apache.log4j.PatternLayout");
		prop.setProperty("log4j.appender.CONSOLE.layout.ConversionPattern", "%d{HH:mm:ss,SSS} [%t] %-5p %C{1} : %m%n");
		PropertyConfigurator.configure(prop);
		
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
