package com.uniovi.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository ur;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@PostConstruct
	public void init() {
	}

	public void add(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		ur.save(user);
	}

	public User getUserByEmail(String email) {
		return ur.findByEmail(email);
	}

}
