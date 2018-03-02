package com.uniovi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uniovi.entities.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {
	

	
}
