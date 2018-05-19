package grimsby.networking.client;

import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AES {
	
	private static final String ALGO = "AES";
	private static byte[] keyValue;
	
	public AES(String key) {
		while (key.length() < 16) {
			key += "A";
		}
		if (key.length() > 16) {
			key = key.substring(0, 16);
		}
		keyValue = key.getBytes();
	}
	
	public String encrypt(String message) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance("AES");
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(message.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}
	
	public String decrypt(String ciphertext) {
		try {
			Key key = generateKey();
			Cipher c = Cipher.getInstance("AES");
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decodedValue = new BASE64Decoder().decodeBuffer(ciphertext);
			byte[] message = c.doFinal(decodedValue);
			return new String(message);
		} catch (Exception e) {
			return "<<<<<<<<<Failed to decrypt>>>>>>>";
		}
	}
	
	public static Key generateKey() {
		Key key = new SecretKeySpec(keyValue, "AES");
		return key;
	}

}