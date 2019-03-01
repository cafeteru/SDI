package com.koinsys.wallapop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koinsys.wallapop.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
}
