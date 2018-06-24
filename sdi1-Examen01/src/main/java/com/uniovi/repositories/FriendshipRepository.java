package com.uniovi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uniovi.entities.Friendship;
import com.uniovi.entities.User;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

	Friendship searchByUsers(User user1, User user2);
}
