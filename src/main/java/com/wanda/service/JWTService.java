package com.wanda.service;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.wanda.utils.exception.CustomException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

	private byte[] secretKey;

	public JWTService() {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");

			SecretKey sk = keyGen.generateKey();

			secretKey = sk.getEncoded();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CustomException("no such algo");
		}
	}

	public String generate(String username) {

		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().claims().add(claims).subject(username).issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis() * 60 * 60 * 10)).and().signWith(getKey()).compact();

	}

	public Key getKey() {

		return Keys.hmacShaKeyFor(secretKey);
	}

}
