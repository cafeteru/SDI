package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uniovi.entities.Request;

public interface RequestRepository extends JpaRepository<Request, Long> {

	List<Request> findAllSentById(Long id);

	Request findBySenderIdAndReceiverId(Long sender, Long receiver);
}
