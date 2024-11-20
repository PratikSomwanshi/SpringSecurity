package com.wanda.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.wanda.entity.Role;
import com.wanda.entity.User;
import com.wanda.repository.RoleRepository;
import com.wanda.repository.UserRepository;
import com.wanda.utils.exception.CustomException;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	@Lazy
	private AuthenticationManager authManager;

	@Autowired
	private JWTService jwtService;

	public User getUserByName(String username) {
		Optional<User> existingUser = this.userRepository.findByUsername(username);

		if (existingUser.isPresent()) {
			return existingUser.get();
		}

		throw new CustomException("User not found");
	}

	public User saveUser(User user) {
		User newUser = new User();

		newUser.setEmail(user.getEmail());
		newUser.setUsername(user.getUsername());

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String hashedPassword = passwordEncoder.encode(user.getPassword());

		newUser.setPassword(hashedPassword);

		Set<Role> newRoles = new HashSet<>();

		user.getRoles().stream().forEach(role -> {
			Optional<Role> existingAutority = this.roleRepository.findByAuthority(role.getAuthority());

			if (existingAutority.isPresent()) {
				newRoles.add(existingAutority.get());
			} else {
				newRoles.add(role);
			}
		});

		newUser.setRoles(newRoles);

		return this.userRepository.save(newUser);
	}

	public String verify(User user) {
		Authentication authenticate = authManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		if (authenticate.isAuthenticated()) {
			return this.jwtService.generate(user.getUsername());
		}

		throw new CustomException("User not found");
	}

}
