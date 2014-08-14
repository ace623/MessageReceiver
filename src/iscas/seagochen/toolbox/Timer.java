package iscas.seagochen.toolbox;

public class Timer {
	// 计时工具
	private long start;
	private long hold;
	private long last;
	
	// 倒计时
	private long timeout;
	private long previous;
	private long current;
	
	// 时间转换
	protected long milliseconds;
	protected long seconds;
	protected long minuters;
	protected long hours;
	protected long days;
	protected long months;
	protected long years;
	
	// 时长
	protected long duration;
	
	private StringBuffer strbuf;
	
	public Timer()
	{
		start = last = hold = 0;
		milliseconds = seconds = minuters = 0;
		hours = days = months = years = 0;
		duration = 0;
		
		resetTimer();
	}
	
	/**
	 * 打印当前的过程时间
	 * @param end
	 * @param start
	 */
	public void printDuration( long end, long start ) {
		duration = end - start;
		System.out.println( duration + "ms" );
	}
	
	/**
	 * 给定结束时间与开始时间，将经过的毫秒时间转换为年月日时分秒
	 * @param end
	 * @param start
	 */
	public void formattedDuration( long end, long start ) {
		duration = end - start;
		
		milliseconds = duration % 1000;
		seconds      = duration / 1000;
		minuters     = seconds  / 60;
		hours        = minuters / 60;
		days         = hours  / 24;
		months       = days   / 30;
		years        = months / 12;
		
		seconds  %= 60;
		minuters %= 60;
		hours    %= 60;
		days     %= 24;
		months   %= 12;
	}
	
	public String getFormattedCurrent()
	{
		last = System.currentTimeMillis();
		String string = getFormattedDuration(last, hold);
		hold = last;
		return string;
	}
	
	public String getFormattedDuration()
	{
		last = System.currentTimeMillis();
		return getFormattedDuration(last, start);
	}

	private String getFormattedDuration( long end, long start ) {
		formattedDuration(end, start);
		
		strbuf = new StringBuffer();
		
		if ( years > 0 )
			strbuf.append( years + "y" + months + "m" + days + "d" + 
					hours + "h" + minuters + "m" + seconds + "s," +  
					milliseconds + "ms" );
		else
		{
			if ( months > 0 )
			strbuf.append( months + "m" + days + "d" + 
					hours + "h" + minuters + "m" + seconds + "s," +  
					milliseconds + "ms" );
			else
			{
				if ( days > 0 )
				strbuf.append( days + "d" + hours + "h" + minuters + "m" +
						seconds + "s," + milliseconds + "ms" );
				else
				{
					if ( hours > 0 )
					strbuf.append( hours + "h" + minuters + "m" +
							seconds + "s," + milliseconds + "ms" );	
					else
					{
						if ( minuters > 0 )
							strbuf.append( minuters + "m" + seconds + "s," + milliseconds + "ms" );
						else {
							if ( seconds > 0 )
								strbuf.append( seconds + "s," + milliseconds + "ms" );
							else {
								strbuf.append( milliseconds + "ms" );
							}
						}
					}
				}
			}
		}
		
		return strbuf.toString();
	}
	
	private void printFormattedDuration( long end, long start ) {
		System.out.println( getFormattedDuration(end, start) );
	}
	
	/**
	 * 计时工具，打印从上一次输出到当前所经历的时间
	 */
	public void printCurrent()
	{
		last = System.currentTimeMillis();
		printFormattedDuration(last, hold);
		hold = last;
	}
	
	/**
	 * 计时工具，打印从计时器开始计时到到当前所经历的时间
	 */
	public void printTotal()
	{
		last = System.currentTimeMillis();
		printFormattedDuration(last, start);
		hold = last;
	}
	
	/**
	 * 计时工具，重置时间
	 */
	public void resetTimer()
	{
		start = hold = last = System.currentTimeMillis();
	}
	
	/**
	 * 设定倒计时时间，单位毫秒
	 * @param millseconds
	 */
	public void setTimeout( long milliseconds )
	{
		timeout  = milliseconds;
		previous = System.currentTimeMillis();
	}
	
	/**
	 * 若超时，返回true
	 * @return 是否超时
	 */
	public boolean isTimeout()
	{
		current = System.currentTimeMillis();
		
		if ( current - previous > timeout )
		{
			previous = System.currentTimeMillis();
			return true;
		}

		return false;
	}
}
