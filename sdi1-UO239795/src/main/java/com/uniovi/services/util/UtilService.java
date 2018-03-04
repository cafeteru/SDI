package com.uniovi.services.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;
import com.uniovi.services.UsersService;

@Service
public class UtilService {
	@Autowired
	private UsersService usersService;

	public User getCurrentUser() {
		UserDetails actual = (UserDetails) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		User user = usersService.getUserByEmail(actual.getUsername());
		return user;
	}
}
