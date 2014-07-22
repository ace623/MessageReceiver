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
	 * 发送字符串数据包至远端服务器
	 * 
	 * @param pack 数据包串
	 * @param ip   远端IP地址，如 127.0.0.1
	 * @param port 远端端口号， 如 12345
	 * @return     数据包发送状态，如数据包正确发送，返回值为0，若参数错误返回值为1，若发送失败返回值为-1
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
