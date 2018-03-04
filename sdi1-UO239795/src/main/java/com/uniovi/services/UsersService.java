package com.uniovi.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;

@Service
public class UsersService {

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@PostConstruct
	public void init() {
	}

	public void add(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		usersRepository.save(user);
	}
	
	public User getUser(Long id) {
		return usersRepository.findOne(id);
	}

	public User getUserByEmail(String email) {
		return usersRepository.findByEmail(email);
	}

	public Page<User> getUsers(Pageable pageable, Long id) {
		return usersRepository.findAllList(pageable, id);
	}

	public Page<User> searchByEmailAndNameAndSurname(Pageable pageable,
			String searchText, Long id) {
		searchText = "%" + searchText + "%";
		return usersRepository.searchByEmailAndNameAndSurname(pageable,
				searchText, id);
	}

}
