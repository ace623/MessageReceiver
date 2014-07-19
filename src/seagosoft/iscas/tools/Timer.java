package seagosoft.iscas.tools;

public class Timer {
	private long start;
	private long hold;
	private long last;
	
	public Timer()
	{
		start = last = hold = System.currentTimeMillis();
	}

	/**
	 * 计时工具，打印从上一次输出到当前所经历的时间
	 */
	public void printCurrent()
	{
		last = System.currentTimeMillis();
		System.out.println("time(cur): " + (last - hold) + "ms");
		hold = last;
	}
	
	/**
	 * 计时工具，打印从上一次输出到当前所经历的时间
	 * @param msg   消息
	 */
	public void printCurrent( String msg )
	{
		last = System.currentTimeMillis();
		System.out.println("time(cur): " + (last - hold) + "ms> " + msg);
		hold = last;
	}
	
	/**
	 * 计时工具，打印从计时器开始计时到到当前所经历的时间
	 */
	public void printTotal()
	{
		last = System.currentTimeMillis(); 
		System.out.println("time(sum): " + (last - start) + "ms");
		hold = last;
	}
	
	/**
	 * 计时工具，打印从计时器开始计时到到当前所经历的时间
	 * @param msg    消息
	 */
	public void printTotal( String msg )
	{
		last = System.currentTimeMillis(); 
		System.out.println("time(sum): " + (last - start) + "ms> " + msg);
		hold = last;		
	}
	
	/**
	 * 计时工具，重置时间
	 */
	public void resetTimer()
	{
		start = hold = last = System.currentTimeMillis();
	}
	
	private long millseconds;
	
	/**
	 * 设定倒计时时间，单位毫秒
	 * @param millseconds
	 */
	public void setTimeout( long millseconds )
	{
		this.millseconds = millseconds;
		hold = System.currentTimeMillis();
	}
	
	public long time()
	{
		return last - hold;
	}
	
	/**
	 * 若超时，返回true
	 * @return 是否超时
	 */
	public boolean isTimeout()
	{
		last = System.currentTimeMillis();
		
		if ( last - hold > millseconds )
		{
			hold = System.currentTimeMillis();
			return true;
		}

		return false;
	}
}
