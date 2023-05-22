package com.moviebooking.auth.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class CustomUserDetails extends org.springframework.security.core.userdetails.User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String email;

	public CustomUserDetails(String username, String password, String email, String id,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.email = email;
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}
	
}
