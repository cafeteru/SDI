package com.uniovi.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.uniovi.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByUserId(Pageable pageable, Long id);

}
