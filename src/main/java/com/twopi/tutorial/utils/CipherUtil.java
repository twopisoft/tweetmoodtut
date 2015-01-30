package com.twopi.tutorial.utils;

import java.util.Map;
import org.jasypt.util.text.BasicTextEncryptor;

/**
 * 
 * @author twopi
 * Utility class for encryption and decryption of parameter values. Uses Jasypt library for
 * actual encryption/decryption.
 */
public class CipherUtil {
	
	private static String KEY_ENV_VAR = "SECRET_KEY";

	/**
	 * Encrypts a value. The key is read from environment. 
	 * @param value
	 * @return Encrypted value.
	 */
	public static String encrypt(String value) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(getKey());
		return textEncryptor.encrypt(value);
	}
	
	/**
	 * Decrypts a value. The key is read from the environment.
	 * @param value - Encrypted value.
	 * @return - Palintext value
	 */
	public static String decrypt(String value) {
		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(getKey());
		return textEncryptor.decrypt(value);
	}
	
	private static String getKey() {
		Map<String, String> env = System.getenv();
		String key = env.get(KEY_ENV_VAR);
		return (key == null ? "" : key);
	}
	
	public static void main(String args[]) {
		if (args.length > 0) {
			for (int i=0; i<args.length; i++) {
				String encrypted = encrypt(args[i]);
				System.out.println(args[i] + " \t\t\t\t " + encrypted);
			}
		} else {
			System.err.println("CipherUtil: No values to encrypt");
		}
	}
}
