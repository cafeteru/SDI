package com.uniovi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.uniovi.entities.User;

public interface UsersRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE (UPPER(u.email) LIKE UPPER(?1) OR "
			+ "LOWER(u.name) LIKE LOWER(?1) OR LOWER(u.surName) LIKE (?1))"
			+ " ORDER BY u.surName ASC")
	Page<User> searchByEmailAndNameAndSurname(Pageable pageable,
			String seachtext);

	@Query("SELECT u FROM User u ORDER BY u.surName ASC")
	Page<User> findAll(Pageable pageable);
}
