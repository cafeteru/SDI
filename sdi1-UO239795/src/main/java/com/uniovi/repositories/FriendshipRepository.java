package com.uniovi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.uniovi.entities.Friendship;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

}
