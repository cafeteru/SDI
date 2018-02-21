package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uniovi.entities.User;
import com.uniovi.servicies.SecurityService;
import com.uniovi.servicies.UsersService;

@Controller
public class UserController {
	@Autowired
	private UsersService us;

	@Autowired
	private SecurityService securityService;

	@GetMapping("/signup")
	public String signUpHtml() {
		return "users/signup";
	}

	@PostMapping("/signup")
	public String signUpHtm(@ModelAttribute("user") User user, Model model) {
		us.add(user);
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
		return "redirect:/home";
	}

	@GetMapping("/login")
	public String login() {
		return "users/login";
	}

	@RequestMapping("/home")
	public String home(Model model) {
		return "users/home";
	}
}
