package com.uniovi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Friendship;
import com.uniovi.repositories.FriendshipRepository;

@Service
public class FriendshipService {
	@Autowired
	private FriendshipRepository friendshipRepository;

	public void add(Friendship friendship) {
		friendshipRepository.save(friendship);
	}

}
