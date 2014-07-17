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
}
