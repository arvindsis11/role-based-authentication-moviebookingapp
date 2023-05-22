package com.moviebooking.auth.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.moviebooking.auth.dto.ResetDto;
import com.moviebooking.auth.dto.SignupDto;
import com.moviebooking.auth.model.User;

public interface UserService{

	public ResponseEntity<?> addUser(SignupDto signupRequest);

	// this will work like a authentication manager for validating user
	//public boolean loginUser(LoginDto loginDto);

	public List<User> getAllUsers();
	
	//replicating spring inbuit class--fix here
	//public Optional<User> getUserByUsername(String username);
	
	public ResponseEntity<?> updatePassword(ResetDto resetDto);

}
