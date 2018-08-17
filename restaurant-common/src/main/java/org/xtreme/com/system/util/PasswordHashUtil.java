package org.xtreme.com.system.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PasswordHashUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordHashUtil.class);

	private static final int KEY_LENGTH = 64; // 512 bits
	private static final int ITERATION_COUNT = 1000;





	private PasswordHashUtil() {

	}



	public static String getSecurePassword(String password, String salt) {
		char[] passwordCharArray = password.toCharArray();
		byte[] res = new byte[1];
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(passwordCharArray, Base64.getDecoder().decode(salt), ITERATION_COUNT,
					KEY_LENGTH);
			SecretKey key = skf.generateSecret(spec);
			res = key.getEncoded();
		} catch (InvalidKeySpecException invalidKey) {
			LOGGER.error(" Invalid Key ", invalidKey);
		} catch (NoSuchAlgorithmException noSuchAlgo) {
			LOGGER.error(" No Such Algorithm exists ", noSuchAlgo);
		}
		return Base64.getEncoder().encodeToString(res);
	}





	public static String decode(String value) {
		byte[] decodedValue = Base64.getDecoder().decode(value); // Basic Base64 decoding
		return new String(decodedValue);
	}





	public static String encode(String password) {
		byte[] passwordEncoded = Base64.getEncoder().encode(password.getBytes());
		return new String(passwordEncoded);
	}





	public static String base32EncodeWithCharactersAndDigits(String data) {

		return StringUtils.replace(new Base32().encodeToString(data.getBytes()), "=", "_");
	}





	public static String base32DecodeWithCharactersAndDigits(String data) {

		return new String(new Base32().decode(StringUtils.replace(data, "_", "=")));
	}





	public static String getSalt() {
		SecureRandom secureRandom;
		byte[] salt = new byte[16];
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
			secureRandom.nextBytes(salt);
		} catch (NoSuchAlgorithmException noSuchAlgo) {
			LOGGER.error(" No Such Algorithm exists ", noSuchAlgo);
		}
		return Base64.getEncoder().encodeToString(salt);
	}
}
