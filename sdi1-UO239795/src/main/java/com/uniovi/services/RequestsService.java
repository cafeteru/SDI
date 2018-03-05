package com.uniovi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Request;
import com.uniovi.repositories.RequestRepository;

@Service
public class RequestsService {
	@Autowired
	private RequestRepository requestRepository;

	public void add(Request request) {
		requestRepository.save(request);
	}

	public List<Request> findSendRequestByUser(Long id) {
		return requestRepository.findAllSentById(id);
	}

	public Request findBySenderIdAndReceiverId(Long sender, Long receiver) {
		return requestRepository.findBySenderIdAndReceiverId(sender, receiver);
	}

	public Request getById(Long id) {
		return requestRepository.findOne(id);
	}

	public void modify(Request request) {
		requestRepository.save(request);
	}
}
