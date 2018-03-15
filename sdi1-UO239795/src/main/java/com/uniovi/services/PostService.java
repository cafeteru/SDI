package com.uniovi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Post;
import com.uniovi.repositories.PostRepository;

@Service
public class PostService {
	@Autowired
	private PostRepository postRepository;

	public void add(Post post) {
		postRepository.save(post);
	}

	public Page<Post> findAll(Pageable pageable, Long id) {
		return postRepository.findAllByUserId(pageable, id);
	}

}
