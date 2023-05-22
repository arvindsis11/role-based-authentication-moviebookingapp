package com.moviebooking.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moviebooking.auth.config.JwtTokenUtil;
import com.moviebooking.auth.dto.LoginDto;
import com.moviebooking.auth.dto.ResetDto;
import com.moviebooking.auth.dto.SignupDto;
import com.moviebooking.auth.model.User;
import com.moviebooking.auth.repository.RoleRepository;
import com.moviebooking.auth.repository.UserRepository;
import com.moviebooking.auth.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private RoleRepository roleRepo;

	@MockBean
	private UserRepository userRepo;

	@MockBean
	private UserService userService;

	@MockBean
	private JwtTokenUtil jwtTokenUtil;

	@Test
	void testRegisterUser() throws Exception {
		// Role roleuser = new Role("101", "ROLE_ADMIN");
		Set<String> roles = new HashSet<>();
		roles.add("ROLE_ADMIN");
		SignupDto signUpObj = new SignupDto("test", "test@gmail.com", roles, "testpsk", "pet", "testpet");
		when(userService.addUser(signUpObj)).thenReturn(new ResponseEntity<>(HttpStatus.OK));
		mockMvc.perform(post("/api/v1.0/auth/signup").content(toJson(signUpObj)).contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

//	@Test
//	void testLoginUser() throws Exception {
//
//		LoginDto loginObj = new LoginDto("admin", "arvind@123");
//		User userObj = new User("admin", "test@gmail.com", "arvind@123", "pet", "testpet");
//		when(userService.getUserByUsername(anyString())).thenReturn(Optional.of(userObj));
//		when(jwtTokenUtil.generatToken2(loginObj)).thenReturn("testToken");
//
//		mockMvc.perform(post("/api/v1.0/auth/login").content(toJson(loginObj)).contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
//
//	}

//	@Test
//	public void testLoginUserInvalidCredentials() throws Exception {
//		LoginDto loginObj = new LoginDto("admin", "arvind@123");
//
//		when(userService.getUserByUsername(loginObj.getUsername())).thenReturn(Optional.empty());
//
//		mockMvc.perform(post("/api/v1.0/auth/login").content(toJson(loginObj)).contentType(MediaType.APPLICATION_JSON)
//				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());
//	}

	@Test
	public void testValidateTokenInvalid() throws Exception {
		String token = "Bearer test-token";

		when(jwtTokenUtil.validateJwtToken("test-token")).thenReturn(false);

		mockMvc.perform(post("/api/v1.0/auth/validate").header("Authorization", token))
				.andExpect(status().isUnauthorized()).andExpect(content().string("Invalid token"));
	}

	@Test
	void testUpdatePassword() throws Exception {
		String username = "testusername";
		ResetDto resetobj = new ResetDto(username, "newpass", "secquestion", "secanswer");
		when(userService.updatePassword(any(ResetDto.class)))
				.thenReturn(new ResponseEntity<>(HttpStatus.OK));

		mockMvc.perform(patch("/api/v1.0/auth/forgot/{username}", username).content(toJson(resetobj))
				.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

	}

	@Test
	void testGetAllUsers() throws Exception {
		User userObj1 = new User("admin", "test@gmail.com", "arvind@123", "pet", "testpet");
		User userObj2 = new User("admin", "test@gmail.com", "arvind@123", "pet", "testpet");
		List<User> userList = Stream.of(userObj1, userObj2).collect(Collectors.toList());
		when(userService.getAllUsers()).thenReturn(userList);
//		ObjectMapper mapper = new ObjectMapper();
		mockMvc.perform(get("/api/v1.0/auth/getAllUsers").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json(toJson(userList)));
	}

	public static String toJson(Object obj) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		String jsonObj = mapper.writeValueAsString(obj);
		return jsonObj;

	}

}
