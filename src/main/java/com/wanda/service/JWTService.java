package com.wanda.service;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.wanda.utils.exception.CustomException;
import com.wanda.utils.response.ErrorResponse;

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



	public void validate(String bearer, UserDetails userDetails) {
		
//		throw new CustomException("checking validate error");
		
		if(bearer.isEmpty()) {
			throw new CustomException("Tocken Not Found");
		}
		
		if(!bearer.startsWith("Bearer")) {
			throw new CustomException("Invalid Tocken new");
		}
		
		String tocken = bearer.substring(7);
		
		System.out.println(tocken);
	
		
	}
	
	public ResponseEntity<ErrorResponse> getErrorResponse(Exception ex) {
		ErrorResponse errorDetails = new ErrorResponse(
	            HttpStatus.NOT_FOUND.value(),	
	            ex.getMessage(),
	            "dummy"
	        );
	    
	    	
	        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
	}



	public String extractUserName() {
		return "manager";
	}

}
