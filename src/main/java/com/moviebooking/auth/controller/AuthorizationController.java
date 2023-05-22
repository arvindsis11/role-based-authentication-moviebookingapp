package com.moviebooking.auth.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moviebooking.auth.config.JwtTokenUtil;
import com.moviebooking.auth.dto.CustomUserDetails;
import com.moviebooking.auth.dto.JwtResponse;
import com.moviebooking.auth.dto.LoginDto;
import com.moviebooking.auth.dto.ResetDto;
import com.moviebooking.auth.dto.SignupDto;
import com.moviebooking.auth.exception.InvalidInputException;
import com.moviebooking.auth.model.User;
import com.moviebooking.auth.repository.RoleRepository;
import com.moviebooking.auth.service.JwtUserDetailsServiceImpl;
import com.moviebooking.auth.service.UserService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1.0/auth") // fix me
public class AuthorizationController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtUserDetailsServiceImpl userDetailsService;

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@RequestBody SignupDto signUpDto) {
		return userService.addUser(signUpDto);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			CustomUserDetails userDetails = userDetailsService.loadUserByUsername(loginDto.getUsername());
			String jwtToken = jwtTokenUtil.generatToken(authentication);
			List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			return ResponseEntity.ok(new JwtResponse(jwtToken, userDetails.getId(), userDetails.getUsername(),
					userDetails.getEmail(), roles));
		} catch (DisabledException e) {
			throw new InvalidInputException("USER_DISABLED");
		} catch (BadCredentialsException e) {
			throw new InvalidInputException("INVALID_CREDENTIALS");
		}
	}

	@PostMapping("/validate")
	public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
		if (jwtTokenUtil.validateJwtToken(token)) {
			Map<String, String> userInfo = new HashMap<>();
			String authToken = token.substring(7);
			String username = jwtTokenUtil.getUserNameFromJwtToken(authToken);
			String role = jwtTokenUtil.getRoleFromToken(authToken);
			userInfo.put(username, role);
			return ResponseEntity.status(HttpStatus.OK).body(userInfo);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
	}

	@PatchMapping("/forgot") // {username} fix--me no need of username here
	public ResponseEntity<?> forgotPassword(@RequestBody ResetDto resetDto) {
		return userService.updatePassword(resetDto);

	}

	@GetMapping("/getAllUsers")
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

}
