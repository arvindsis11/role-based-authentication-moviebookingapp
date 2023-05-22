package com.moviebooking.auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.moviebooking.auth.model.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(String name);
}