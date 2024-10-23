package com.reviewping.coflo.global.crypto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import com.reviewping.coflo.global.error.ErrorCode;
import com.reviewping.coflo.global.error.exception.BusinessException;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {
	private final String iv;
	private final Key keySpec;

	public CryptoConverter(@Value("${crypto-key}") String key) {
		this.iv = key.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes(StandardCharsets.UTF_8);
		int len = b.length;
		if(len > keyBytes.length){
			len = keyBytes.length;
		}
		System.arraycopy(b, 0, keyBytes, 0, len);
		this.keySpec = new SecretKeySpec(keyBytes, "AES");
	}

	@Override
	public String convertToDatabaseColumn(String str) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			byte[] encrypted = c.doFinal(str.getBytes(StandardCharsets.UTF_8));
			return new String(Base64.getEncoder().encode(encrypted));
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.ENCRYPTION_ERROR);
		}
	}

	@Override
	public String convertToEntityAttribute(String str) {
		try {
			Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
			c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
			byte[] byteStr = Base64.getDecoder().decode(str.getBytes());
			return new String(c.doFinal(byteStr), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new BusinessException(ErrorCode.DECRYPTION_ERROR);
		}
	}
}
