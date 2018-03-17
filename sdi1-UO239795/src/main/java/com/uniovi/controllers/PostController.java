package com.uniovi.controllers;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.uniovi.entities.Post;
import com.uniovi.entities.User;
import com.uniovi.services.LogService;
import com.uniovi.services.PostService;
import com.uniovi.services.UsersService;

@Controller
public class PostController {
	@Autowired
	private PostService postService;

	@Autowired
	private UsersService usersService;

	private LogService logService = new LogService(this);

	@GetMapping("/post/add")
	public String formPost(Model model) {
		model.addAttribute("post", new Post());
		return "posts/add";
	}

	@PostMapping("/post/add")
	public String addPost(@ModelAttribute Post post, Principal principal,
			@RequestParam(value = "imagen", required = false) MultipartFile img) {
		try {
			User user = usersService.getUserByEmail(principal.getName());
			if (!img.getOriginalFilename().equals("")) {
				String fileName = postService.saveImg(img);
				logService.info(principal.getName()
						+ " ha realizado una nueva publicación");
				post.setImg("/imgUser/" + fileName);
			}
			post.setUser(user);
			post.setDate(new Date(System.currentTimeMillis()));
			postService.add(post);
			return "redirect:/post/list";
		} catch (IOException e) {
			return "redirect:/post/add";
		}
	}

	@GetMapping("/post/list")
	public String listPost(Model model, Pageable pageable,
			Principal principal) {
		User user = usersService.getUserByEmail(principal.getName());
		Page<Post> page = postService.findAll(pageable, user.getId());
		List<Post> posts = page.getContent();
		model.addAttribute("list", posts);
		model.addAttribute("page", page);
		return "posts/list";
	}

	@GetMapping("/post/friends/{id}")
	public String friendsPost(Model model, Pageable pageable,
			@PathVariable Long id) {
		User user = usersService.getUser(id);
		Page<Post> page = postService.findAll(pageable, user.getId());
		List<Post> posts = page.getContent();
		model.addAttribute("friend", user.getName() + " " + user.getSurName());
		model.addAttribute("list", posts);
		model.addAttribute("page", page);
		return "posts/friends";
	}

}
