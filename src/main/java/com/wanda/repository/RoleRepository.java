package com.wanda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wanda.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	public Optional<Role> findByAuthority(String authority);
}
