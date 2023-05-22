package com.moviebooking.auth.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.moviebooking.auth.model.User;

public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
	
//	@Query("SELECT u FROM User u WHERE u.username = :username AND u.password = :password")
//	Optional<User> validateUser(String username,String password);//verify username and password
}