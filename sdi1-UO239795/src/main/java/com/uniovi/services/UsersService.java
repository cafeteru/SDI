package com.uniovi.services;

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

	public void add(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		usersRepository.save(user);
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

	public void modify(User user) {
		usersRepository.save(user);
	}

	public User getUser(Long id) {
		return usersRepository.findOne(id);
	}

	public Page<User> findAllByRequestReceiverId(Pageable pageable, Long id) {
		return usersRepository.findAllByRequestReceiverId(pageable, id);
	}

	public Page<User> findAllFriendsById(Pageable pageable, Long id) {
		return usersRepository.findAllFriendsById(pageable, id);
	}

	public void delete(Long id) {
		usersRepository.delete(id);
	}

}
