package br.usp.icmc.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashCode {

	private static String stringHexa(byte[] bytes) {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
			int parteBaixa = bytes[i] & 0xf;
			if (parteAlta == 0)
				s.append('0');
			s.append(Integer.toHexString(parteAlta | parteBaixa));
		}
		return s.toString();
	}

	private static byte[] calc(String frase, String algoritmo) {
		try {
			MessageDigest md = MessageDigest.getInstance(algoritmo);
			md.update(frase.getBytes());
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	public static String md5(String frase) {
		return stringHexa(calc(frase, "MD5"));
	}

	public static String sha1(String frase) {
		return stringHexa(calc(frase, "SHA-1"));
	}

	public static String sha256(String frase) {
		return stringHexa(calc(frase, "SHA-256"));
	}
}
