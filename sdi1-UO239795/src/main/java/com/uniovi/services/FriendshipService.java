package com.uniovi.services;

import com.uniovi.entities.Friendship;
import com.uniovi.entities.User;
import com.uniovi.repositories.FriendshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendshipService {
	@Autowired
	private FriendshipRepository friendshipRepository;

	public void add(Friendship friendship) {
		friendshipRepository.save(friendship);
	}

	public Friendship findByFriends(User user1, User user2) {
		return friendshipRepository.searchByUsers(user1, user2);
	}

}
