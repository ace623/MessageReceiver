package seagosoft.iscas.socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendPackageToServer {

	private boolean checkIp(String ip)
	{
		String regex = "^((([0-9])|([1-9][0-9])|(1\\d{1,2})|(2[0-4][0-9])|(25[0-5]))\\.){3}"
				+ "(([0-9])|([1-9][0-9])|(1\\d{1,2})|(2[0-4][0-9])|(25[0-5]))$";
		Pattern pat = Pattern.compile(regex);
		Matcher mat = pat.matcher(ip);
		if(mat.find())
		   return true;
		else
			return false;
	}
	
	/**
	 * �����ַ������ݰ���Զ�˷�����
	 * 
	 * @param pack ���ݰ���
	 * @param ip   Զ��IP��ַ���� 127.0.0.1
	 * @param port Զ�˶˿ںţ� �� 12345
	 * @return     ���ݰ�����״̬�������ݰ���ȷ���ͣ�����ֵΪ0�����������󷵻�ֵΪ1��������ʧ�ܷ���ֵΪ-1
	 */
	public int send(byte[] pack, String ip, int port)
	{
		if(pack == null)
		{
			System.out.println("Package is null!");
			return 1;
		}
		if(!checkIp(ip))
		{
			System.out.println("Invalid ip: "+ip);
			return 1;
		}
		if(port < 0)
		{
			System.out.println("Invalid port: "+port);
			return 1;
		}
		
		InetAddress addr = null;
		try {
			addr = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		Socket sock = null;
		
		try {
			sock = new Socket(addr, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		System.out.println(sock);
		
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		try {
			out.write(pack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		try {
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		return 0;
	}

//	public static void main(String[] args)
//	{
//		sendPack sp = new sendPack();
//		System.out.println(sp.send(new byte[10], "255.0.0.0", -3));
//	}
}
