package util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class <code>MD5Encrypt</code> berfungsi mengenkripsi sebuah string. Class ini 
 * hanya berisikan function static. Jadi tidak perlu membuat object baru.
 * @author Yohanes Surya
 */
public class MD5Encrypt {
	/**
	 * Mengenkripsi sebuah string
	 * @param string <code>String</code> yang akan dienkripsi
	 * @return <code>String</code> yang sudah di enkripsi
	 */
	public static String encrypt(String string){
		MessageDigest mdEnc;
		String md5=null;
		try {
			mdEnc = MessageDigest.getInstance("MD5");
			mdEnc.update(string.getBytes(), 0, string.length());
			md5= new BigInteger(1,mdEnc.digest()).toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return md5;
	}
}
