package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.annotation.SessionScope;

import com.uniovi.entities.User;
import com.uniovi.services.UsersService;

@Controller
@SessionScope
public class UserController {
	@Autowired
	private UsersService us;

	@RequestMapping("/signup")
	public String signUpHtml() {
		return "users/signup";
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public String signUpHtm(@ModelAttribute User user) {
		us.add(user);
		return "redirect:/home";
	}

	@GetMapping("login")
	public String login() {
		return "users/login";
	}

	@RequestMapping("/home")
	public String getList() {
		return "users/home";
	}
}
