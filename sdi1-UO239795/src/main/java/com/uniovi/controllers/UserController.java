package com.uniovi.controllers;

import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniovi.entities.User;
import com.uniovi.services.RolesService;
import com.uniovi.services.SecurityService;
import com.uniovi.services.UsersService;
import com.uniovi.validators.SignUpFormValidator;

@Controller
public class UserController {
	@Autowired
	private UsersService usersService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private RolesService rolesService;

	@Autowired
	private SignUpFormValidator signUpFormValidator;

	@GetMapping("/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/signup")
	public String setUser(@Validated User user, BindingResult result,
			Model model) {
		signUpFormValidator.validate(user, result);
		if (result.hasErrors()) {
			return "signup";
		}
		user.setRole(rolesService.getUser());
		usersService.add(user);
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm());
		return "redirect:home";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping(value = "/home")
	public String home(Model model) {
		return "home";
	}

	@GetMapping("/user/list")
	public String getListado(Model model, Pageable pageable,
			@RequestParam(value = "", required = false) String searchText) {
		Page<User> users = new PageImpl<User>(new LinkedList<User>());
		if (searchText != null && !searchText.isEmpty()) {
			users = usersService.searchByEmailAndNameAndSurname(pageable,
					searchText);
		} else {
			users = usersService.getUsers(pageable);
		}
		Object a = SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		UserDetails actual = (UserDetails) a;
		System.out.println(actual.getUsername());
		model.addAttribute("usersList", users.getContent());
		model.addAttribute("page", users);
		return "users/list";
	}
}
