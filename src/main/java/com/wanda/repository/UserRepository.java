package com.wanda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanda.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public Optional<User> findByUsername(String username);
}
