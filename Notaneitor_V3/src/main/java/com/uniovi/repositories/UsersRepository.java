package com.uniovi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.User;

public interface UsersRepository extends CrudRepository<User, Long> {
	User findByDni(String dni);

	@Query("SELECT u FROM User u WHERE (UPPER(u.dni) LIKE UPPER(?1) OR "
			+ "LOWER(u.name) LIKE LOWER(?1) OR LOWER(u.lastName) LIKE (?1))")
	Page<User> searchByDniAndNameAndLastname(Pageable pageable,
			String seachtext);

	@Query("SELECT u FROM User u ORDER BY u.id ASC")
	Page<User> findAll(Pageable pageable);
}
