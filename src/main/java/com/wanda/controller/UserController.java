package com.wanda.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.wanda.entity.User;
import com.wanda.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping
	public String getHome() {

		return "Home Page";
	}

	@GetMapping("/private")
	public String getPrivate() {
		return "Private Page";
	}

	@PostMapping("/register")
	public User register(@RequestBody User user) {

		return this.userService.saveUser(user);

	}

	@PostMapping("/login")
	public String login(@RequestBody User user) {
		return this.userService.verify(user);
//		return "Hello";
//		throw new CustomException("checking exception");
	}
}
