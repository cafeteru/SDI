package com.uniovi.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uniovi.entities.User;
import com.uniovi.services.LogService;
import com.uniovi.services.UsersService;

@Controller
public class FriendshipController {
	@Autowired
	private UsersService usersService;

	private LogService logService = new LogService(this);

	@GetMapping("/friends")
	public String showFriends(Model model, Pageable pageable,
			Principal principal) {
		logService.info(principal.getName() + " lista los amistades");
		User user = usersService.getUserByEmail(principal.getName());
		Page<User> users = usersService.findAllFriendsById(pageable,
				user.getId());
		model.addAttribute("usersList", users.getContent());
		model.addAttribute("page", users);
		return "/users/friends";
	}
}
