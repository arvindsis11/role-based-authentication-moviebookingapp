package com.moviebooking.auth.service;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moviebooking.auth.dto.CustomUserDetails;
import com.moviebooking.auth.model.Role;
import com.moviebooking.auth.model.User;
import com.moviebooking.auth.repository.UserRepository;

@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
	/**
	 * experimental--method--security
	 */

	@Autowired
	private UserRepository userRepository;

	@Override
	public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUsername(username);
		if (!user.isPresent()) {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
		User userDetails = user.get();
		CustomUserDetails customUserDetails = new CustomUserDetails(userDetails.getUsername(),
				userDetails.getPassword(), userDetails.getEmail(), userDetails.getId(),
				mapRolesToAuthorities(userDetails.getRoles()));
		return customUserDetails;
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

}
