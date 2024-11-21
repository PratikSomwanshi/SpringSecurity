package com.wanda.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wanda.utils.exception.CustomException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Service
public class JWTService {
	
	private SecretKey secretKey;
	
	private String mainTocken;
	
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
					.claims(claims)
					.subject(username)
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis() + 60 * 60 * 10))
					.signWith( getKey(), Jwts.SIG.HS256 )
					.compact();

	}

	public SecretKey getKey() {
		
		
		return this.secretKey;
		
		
	}
	
	public void extractTocken(String bearer) {

		if(bearer.isEmpty()) {
			throw new CustomException("Tocken Not Found");
		}
		
		if(!bearer.startsWith("Bearer")) {
			throw new CustomException("Invalid Tocken new");
		}
		
		String tocken = bearer.substring(7);
		
		System.out.println(tocken);
		
		this.mainTocken = tocken;
		
		if(!isTockenValid(tocken)) {
			throw new CustomException("Tocken Expired");
		}
		
		
	}



	public void validate(UserDetails userDetails) {
		
		
		var isValidUser = userDetails.getUsername().equals(this.extractUserName()) && isTockenValid(mainTocken);
		
		if(!isValidUser) {
			throw new CustomException("User not valid");
		}
		
		
		
	}
	
	



	public String extractUserName() {
		return this.getClaims(mainTocken).getSubject();
	}
	
	
	public Boolean isTockenValid(String tocken) {
		return this.getClaims(tocken).getExpiration().after(new Date());
	}
	
	public Claims getClaims(String tocken) {
		 return Jwts
				 	.parser()
		            .verifyWith(this.secretKey)
		            .build()
		            .parseSignedClaims(tocken)
		            .getPayload();
	}

}
