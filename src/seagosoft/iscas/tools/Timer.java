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
	 * ��ʱ���ߣ���ӡ����һ���������ǰ��������ʱ��
	 */
	public void printCurrent()
	{
		last = System.currentTimeMillis();
		System.out.println("time(cur): " + (last - hold) + "ms");
		hold = last;
	}
	
	/**
	 * ��ʱ���ߣ���ӡ����һ���������ǰ��������ʱ��
	 * @param msg   ��Ϣ
	 */
	public void printCurrent( String msg )
	{
		last = System.currentTimeMillis();
		System.out.println("time(cur): " + (last - hold) + "ms> " + msg);
		hold = last;
	}
	
	/**
	 * ��ʱ���ߣ���ӡ�Ӽ�ʱ����ʼ��ʱ������ǰ��������ʱ��
	 */
	public void printTotal()
	{
		last = System.currentTimeMillis(); 
		System.out.println("time(sum): " + (last - start) + "ms");
		hold = last;
	}
	
	/**
	 * ��ʱ���ߣ���ӡ�Ӽ�ʱ����ʼ��ʱ������ǰ��������ʱ��
	 * @param msg    ��Ϣ
	 */
	public void printTotal( String msg )
	{
		last = System.currentTimeMillis(); 
		System.out.println("time(sum): " + (last - start) + "ms> " + msg);
		hold = last;		
	}
	
	/**
	 * ��ʱ���ߣ�����ʱ��
	 */
	public void resetTimer()
	{
		start = hold = last = System.currentTimeMillis();
	}
	
	private long millseconds;
	
	/**
	 * �趨����ʱʱ�䣬��λ����
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
	 * ����ʱ������true
	 * @return �Ƿ�ʱ
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
