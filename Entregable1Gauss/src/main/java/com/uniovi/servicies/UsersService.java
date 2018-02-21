package com.uniovi.servicies;

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

	public void add(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		ur.save(user);
	}

	public User findByEmail(String email) {
		return ur.findByEmail(email);
	}

}
