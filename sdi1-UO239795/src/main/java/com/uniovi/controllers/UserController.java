package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.uniovi.entities.User;
import com.uniovi.services.UsersService;

@Controller
public class UserController {
	@Autowired
	private UsersService us;
	int contador = 0;

	@GetMapping("/signup")
	public String signUpHtml() {
		return "users/signup";
	}

	@PostMapping("/signup")
	public String signUpHtm(@ModelAttribute User user, Model model) {
		boolean added = us.add(user);
		if (added) {
			return "redirect:/home";
		}
		model.addAttribute("added", added);
		return "users/signup";
	}

	@GetMapping("/login")
	public String login() {
		return "users/login";
	}

	@GetMapping("/home")
	public String getList() {
		return "users/home";
	}
}
