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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.uniovi.entities.Post;
import com.uniovi.entities.User;
import com.uniovi.services.FriendshipService;
import com.uniovi.services.LogService;
import com.uniovi.services.PostService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.PostValidator;

@Controller
public class PostController {
	@Autowired
	private PostService postService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private FriendshipService friendshipService;
	@Autowired
	private PostValidator postValidator;

	private LogService logService = new LogService(this);

	@GetMapping("/post/add")
	public String formPost(Model model) {
		model.addAttribute("post", new Post());
		return "posts/add";
	}

	@PostMapping("/post/add")
	public String addPost(@ModelAttribute Post post,
			@RequestParam(value = "imgn", required = false) MultipartFile img,
			Principal principal, BindingResult result) {
		try {
			postValidator.validate(post, result);
			if (result.hasErrors()) {
				logService.error(
						"Usuario introdujo mal los datos de la publicación");
				return "/posts/add";
			}
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
			@PathVariable Long id, Principal principal) {
		User actual = usersService.getUserByEmail(principal.getName());
		User user = usersService.getUser(id);
		if (friendshipService.findByFriends(actual, user) == null) {
			return "redirect:/home";
		}
		Page<Post> page = postService.findAll(pageable, user.getId());
		List<Post> posts = page.getContent();
		model.addAttribute("friend", user.getName() + " " + user.getSurName());
		model.addAttribute("list", posts);
		model.addAttribute("page", page);
		return "posts/friends";

	}

}
