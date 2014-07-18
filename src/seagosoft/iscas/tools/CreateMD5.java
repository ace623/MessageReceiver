package seagosoft.iscas.tools;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author beryl
 * 
 */
public class CreateMD5
{
	
	private String Date;
	private String Address;
	private String CarNumber;
	private String nid;
	
	public CreateMD5(String Date, String Address, String CarNumber)
	{
		this.Date = Date;
		this.Address = Address;
		this.CarNumber = CarNumber;
	}

	private String byteToHex(byte value, int minlength)
	{ 
	    String s = Integer.toHexString(value & 0xff); 
	    if (s.length() < minlength) { 
	        for (int i = 0; i < (minlength - s.length()); i++) 
	            s = "0" + s; 
	    } 
	    return s; 
	} 

	private byte[] MD5(byte[] value)
	{ 
	    try { 
	        MessageDigest md = MessageDigest.getInstance("MD5"); 
	        md.update(value);                
	        return md.digest(); 
	    } catch (NoSuchAlgorithmException e) { 
	        e.printStackTrace(); 
	    } 
	    return null; 
	}
	

	public String MD5(String value)
	{
		byte[] buf = MD5(value.getBytes());
		String tmp = ""; 
		for (int i = 0; i < buf.length; i++) 
			tmp = tmp + byteToHex(buf[i], 2); 
	    return tmp.toUpperCase();
	}
	
	
	public String createMD5()
	{
		if(CarNumber==null || CarNumber.equals("摩托") || CarNumber.equals("摩托车") || CarNumber.endsWith("无车牌")|| CarNumber.endsWith("无牌") || CarNumber.equals("") || CarNumber.equals("未识别") ){
			long temp = (long) (Math.random()*9999999);
			CarNumber = Long.toString(temp);
			//System.out.println("wu che pai: " + CarNumber);
		}
		if (Address == null || Address.equals("") == true){
			long temp = (long) (Math.random()*999999999);
			Address = Long.toString(temp);
		}
		String md5=Date+Address+CarNumber;
    	nid = MD5(md5);
    	return nid;		
	}

	public void setDate(String date) { Date = date; }

	public void setAddress(String address) { Address = address; }

	public void setCarNumber(String carNumber) { CarNumber = carNumber; }

	public String getNid() { return nid; }
	
	
}

