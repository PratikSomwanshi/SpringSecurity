package com.wanda.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.wanda.utils.exception.CustomException;

import io.jsonwebtoken.Jwts;

@Service
public class JWTService {
	
	private SecretKey secretKey;
	
	public JWTService() {
		try {
			KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
			
			keyGenerator.init(256);
			
			
			this.secretKey = keyGenerator.generateKey();
			
			 String base64EncodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
		        System.out.println("Base64 Encoded Secret Key: " + base64EncodedKey);


		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new CustomException("no such alorithm present");
		}
	}



	public String generate(String username) {

		Map<String, Object> claims = new HashMap<>();
		claims.put("name", "rohan");

		return Jwts
					.builder()
					.claims()
					.add(claims)
					.subject(username)
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 10))
					.and()
					.signWith( getKey(), Jwts.SIG.HS256 )
					.compact();

	}

	public SecretKey getKey() {
		
		
		return this.secretKey;
		
		
	}

}
