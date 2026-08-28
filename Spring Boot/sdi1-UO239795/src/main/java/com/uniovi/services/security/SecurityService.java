package com.uniovi.services.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;;

@Service
public class SecurityService {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
	
	private static final Logger logger = LoggerFactory
			.getLogger(SecurityService.class);

	public String findLoggedInEmail() {
		Object userDetails = SecurityContextHolder.getContext()
				.getAuthentication().getDetails();
		if (userDetails instanceof UserDetails) {
			return ((UserDetails) userDetails).getUsername();
		}
		return null;
	}

	private SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	public void autoLogin(String email, String password, HttpServletRequest request, HttpServletResponse response) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(email);
		UsernamePasswordAuthenticationToken aToken;
		aToken = new UsernamePasswordAuthenticationToken(userDetails, password,
				userDetails.getAuthorities());
		Authentication auth = authenticationManager.authenticate(aToken);
		if (auth.isAuthenticated()) {
			SecurityContextHolder.getContext().setAuthentication(auth);
			securityContextRepository.saveContext(SecurityContextHolder.getContext(), request, response);
			logger.debug(String.format("Auto login %s successfully!", email));
		}
	}
}




