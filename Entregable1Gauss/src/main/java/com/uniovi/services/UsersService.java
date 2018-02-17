package com.uniovi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository ur;

	public void add(User user) {
		User aux = ur.findByEmail(user.getEmail());
		System.out.println(aux);
		if (aux == null)
			ur.save(user);
	}

}
