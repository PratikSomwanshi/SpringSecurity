package com.wanda.filters;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wanda.service.CustomUserDetailService;
import com.wanda.service.JWTService;
import com.wanda.utils.exception.CustomException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter{
	
	@Autowired
	private JWTService jwtService;
	
	@Autowired
	private org.springframework.context.ApplicationContext context;
	
	private String username;
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println("jwt filter");
		
		String bearer = request.getHeader("Authorization");
		
		
		
		try {
			
			
			this.username = this.jwtService.extractUserName();
			
			
			if(!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
				
				System.out.println("inside not empty");
				
				UserDetails userDetails = this.context.getBean(CustomUserDetailService.class).loadUserByUsername(this.username);
				
				
				this.jwtService.validate(bearer, userDetails);
				
				UsernamePasswordAuthenticationToken authTocken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				authTocken.setDetails(
						new WebAuthenticationDetailsSource()
															.buildDetails(request)
						);
				
				SecurityContextHolder.getContext().setAuthentication(authTocken);
				
			}
			
			
			filterChain.doFilter(request, response);

		} catch (CustomException e) {
			
			response.sendError(200, e.getMessage());
		} catch (Exception e) {
			response.sendError(200, "Internal Server Error");
			return;
		}
		
		
		
		
		
	}
	
	
	
}
