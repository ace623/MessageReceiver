package seagosoft.iscas.tools;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GenerateSignature
{
	private byte[] bytesOfMD5;
	BigInteger bigInt;
	
	public static final String USE_MD5_CHECKSUM = "MD5";
	public static final String USE_WHIRLPOOL_CHECKSUM = "WHIRLPOOL";
	
	/**
	 * 计算文本数据的哈希值
	 * @param context   文本数据
	 * @param signature 哈希方法
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public void genHashChecksum( String context, String signature ) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		MessageDigest md5 = MessageDigest.getInstance(signature);
		md5.reset();
		md5.update(context.getBytes("UTF-8"));
		
		bytesOfMD5 = md5.digest();
		
		bigInt = new BigInteger(1,bytesOfMD5);
	}
	
	public byte[] getBytesOfMD5() { return bytesOfMD5; }
	
	public String getStringOfMD5( int radix ) { return bigInt.toString(radix); }
}