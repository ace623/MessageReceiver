package iscas.seagochen.toolbox;

public class Timer {
	// ��ʱ����
	private long start;
	private long hold;
	private long last;
	
	// ����ʱ
	private long timeout;
	private long previous;
	private long current;
	
	// ʱ��ת��
	protected long milliseconds;
	protected long seconds;
	protected long minuters;
	protected long hours;
	protected long days;
	protected long months;
	protected long years;
	
	// ʱ��
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
	 * ��ӡ��ǰ�Ĺ���ʱ��
	 * @param end
	 * @param start
	 */
	public void printDuration( long end, long start ) {
		duration = end - start;
		System.out.println( duration + "ms" );
	}
	
	/**
	 * ��������ʱ���뿪ʼʱ�䣬�������ĺ���ʱ��ת��Ϊ������ʱ����
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
	 * ��ʱ���ߣ���ӡ����һ���������ǰ��������ʱ��
	 */
	public void printCurrent()
	{
		last = System.currentTimeMillis();
		printFormattedDuration(last, hold);
		hold = last;
	}
	
	/**
	 * ��ʱ���ߣ���ӡ�Ӽ�ʱ����ʼ��ʱ������ǰ��������ʱ��
	 */
	public void printTotal()
	{
		last = System.currentTimeMillis();
		printFormattedDuration(last, start);
		hold = last;
	}
	
	/**
	 * ��ʱ���ߣ�����ʱ��
	 */
	public void resetTimer()
	{
		start = hold = last = System.currentTimeMillis();
	}
	
	/**
	 * �趨����ʱʱ�䣬��λ����
	 * @param millseconds
	 */
	public void setTimeout( long milliseconds )
	{
		timeout  = milliseconds;
		previous = System.currentTimeMillis();
	}
	
	/**
	 * ����ʱ������true
	 * @return �Ƿ�ʱ
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
