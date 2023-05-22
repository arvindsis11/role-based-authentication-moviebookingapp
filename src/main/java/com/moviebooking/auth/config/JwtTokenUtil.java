package com.moviebooking.auth.config;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.moviebooking.auth.dto.CustomUserDetails;
import com.moviebooking.auth.exception.InvalidInputException;
import com.moviebooking.auth.service.UserService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {

	@Value("${authentication.app.jwtSecret}")
	private String jwtSecret;

	@Value("${authentication.app.jwtTime}")
	private int jwtExpireTime;

	@Autowired
	UserService userService;

	public String generatToken(Authentication authentication) {

		CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
		List<String> roleNames = userPrincipal.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		String userRole = roleNames.get(0).toString();
		System.err.println("generating token method :: " + userPrincipal.getUsername());
		return Jwts.builder().setSubject((userPrincipal.getUsername())).claim("role",userRole)
				.setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + jwtExpireTime))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public Claims getJwtClaims(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return claims;
	}

	// will need this later
	public String getRoleFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();

		String role = (String) claims.get("role");

		return role;
	}

	public boolean validateJwtToken(String token) {// fix me
		String authToken = null;
		String user = null;
		if (token != null && token.startsWith("Bearer ")) {
			authToken = token.substring(7);
			try {
				user = getUserNameFromJwtToken(authToken);
				System.out.println(user);// fix
				return true;
			} catch (Exception e) {
				throw new InvalidInputException("invalid token");
			}
		}
		return false;
	}

}
