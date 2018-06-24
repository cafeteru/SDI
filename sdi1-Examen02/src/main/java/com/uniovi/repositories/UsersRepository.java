package com.uniovi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uniovi.entities.User;

public interface UsersRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	Page<User> findAllList(Pageable pageable, Long id);

	Page<User> searchByEmailAndNameAndSurname(Pageable pageable,
			String seachtext, Long id);

	Page<User> findAllByRequestReceiverId(Pageable pageable, Long id);

	Page<User> findAllFriendsById(Pageable pageable, Long id);
}
