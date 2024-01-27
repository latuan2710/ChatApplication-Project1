package com.chat.infrastructure.services;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.chat.usecases.adapters.Hasher;

public class SHA256Hasher implements Hasher {

	@Override
	public String hash(String orginal) {
		try {
			MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = sha256.digest(orginal.getBytes());
			StringBuilder hexString = new StringBuilder();

			for (byte hashByte : hashBytes) {
				String hex = Integer.toHexString(0xff & hashByte);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (NoSuchAlgorithmException exception) {
			exception.printStackTrace();
			return null;
		}
	}
}
