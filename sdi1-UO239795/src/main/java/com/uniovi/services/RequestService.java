package com.uniovi.services;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Request;
import com.uniovi.repositories.RequestRepository;

@Service
public class RequestService {

	@Autowired
	private RequestRepository requestRepository;

	public Set<Request> findSendRequestByUser(Long id) {
		return null;
	}

}
