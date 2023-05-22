package com.moviebooking.auth.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.moviebooking.auth.dto.LoginDto;
import com.moviebooking.auth.dto.ResetDto;
import com.moviebooking.auth.dto.SignupDto;
import com.moviebooking.auth.model.Role;
import com.moviebooking.auth.model.User;
import com.moviebooking.auth.repository.RoleRepository;
import com.moviebooking.auth.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
	@Mock
	private UserRepository userRepo;

	@Mock
	private RoleRepository roleRepo;

	@InjectMocks
	private UserServiceImpl userService;

	
	@Test
	public void testAddUser() {

		Role customerRole = new Role("101", "ROLE_CUSTOMER");
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_ADMIN");
		SignupDto signupDto = new SignupDto("arvind", "avind@test.com", roles, "password123", "pet", "cat");

		when(userRepo.existsByUsername(anyString())).thenReturn(false);
		when(userRepo.existsByEmail(anyString())).thenReturn(false);
		when(roleRepo.findByName(anyString())).thenReturn(Optional.of(customerRole));
		when(userRepo.save(any(User.class))).thenReturn(null);

		ResponseEntity<?> response = userService.addUser(signupDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("User registered successfully", response.getBody());
	}

//	@Test
//	public void testLoginUser() {
//		LoginDto loginDto = new LoginDto("admin", "admin123");
//		User user = new User("admin", "arvind@test.com", "admin123", "pet", "test");
//		when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
//
//		boolean result = userService.loginUser(loginDto);
//
//		assertTrue(result);
//	}

	@Test
	public void testUpdatePassword() {
		ResetDto resetDto = new ResetDto("admin", "newpassword", "pet", "test");
		User user = new User("admin", "arvind@test.com", "oldpassword", "pet", "test");
		when(userRepo.existsByUsername(anyString())).thenReturn(true);
		when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
		when(userRepo.save(any(User.class))).thenReturn(null);

		ResponseEntity<?> response = userService.updatePassword( resetDto);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("changed password successfully!", response.getBody());
	}

//	@Test
//	public void testGetUserByUsername() {
//		User user = new User("admin", "arvind@test.com", "testpassword", "pet", "test");
//		when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
//
//		Optional<User> result = userService.getUserByUsername("testuser");
//
//		assertTrue(result.isPresent());
//		assertEquals(user, result.get());
//	}
	
	@Test
	public void testGetAllUsers() {
		User user1 = new User("admin", "arvind@test.com", "testpassword", "pet", "test");
		User user2 = new User("user", "user@test.com", "testpassword", "pet", "test");
		List<User> userList = Stream.of(user1,user2).collect(Collectors.toList());
		when(userRepo.findAll()).thenReturn(userList);
		assertEquals(userService.getAllUsers().size(),2);
	}
}
