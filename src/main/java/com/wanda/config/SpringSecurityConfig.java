package com.wanda.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.wanda.filters.JWTFilter;
import com.wanda.service.CustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

	@Autowired
	private CustomUserDetailService userDetailService;
	
	@Autowired
	private JWTFilter jwtFilter;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity auth) {

		try {
			auth
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						req -> {
							req
								.requestMatchers("/", "/login", "/register").permitAll()
								.requestMatchers("private", "user").hasAnyAuthority("USER", "MANAGER")
								.requestMatchers("manager").hasAuthority("MANAGER")
								.anyRequest().permitAll();
						})
				.httpBasic(Customizer.withDefaults())
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);


			return auth.build();
		} catch (Exception e) {
			e.printStackTrace();
//			throw new CustomException(e.getMessage());
			return null;
		}

	}

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

		provider.setUserDetailsService(userDetailService);
		provider.setPasswordEncoder(bCryptPasswordEncoder());

		return provider;
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

}
