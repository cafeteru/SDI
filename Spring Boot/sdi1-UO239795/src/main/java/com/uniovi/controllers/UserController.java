package com.uniovi.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniovi.entities.User;
import com.uniovi.services.RequestsService;
import com.uniovi.services.RolesService;
import com.uniovi.services.UsersService;
import com.uniovi.services.security.SecurityService;
import com.uniovi.services.util.LogService;
import com.uniovi.validators.SignUpFormValidator;

@Controller
public class UserController {
	@Autowired
	private UsersService usersService;

	@Autowired
	private RequestsService requestsService;

	@Autowired
	private SecurityService securityService;

	@Autowired
	private RolesService rolesService;

	@Autowired
	private SignUpFormValidator signUpFormValidator;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private LogService logService = new LogService(this);

	@GetMapping("/signup")
	public String signUp(Model model) {
		logService.info("User tries to register");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/signup")
	public String signupPost(@Validated User user, BindingResult result,
			Model model, HttpServletRequest request, HttpServletResponse response) {
		signUpFormValidator.validate(user, result);
		if (result.hasErrors()) {
			logService.error("User introduced wrong data");
			return "signup";
		}
		logService.info("User registered successfully as "
				+ user.getEmail());
		user.setRole(rolesService.getUser());
		usersService.add(user);
		securityService.autoLogin(user.getEmail(), user.getPasswordConfirm(), request, response);
		return "redirect:home";
	}

	@GetMapping("/login")
	public String login() {
		logService.info("User tries to login");
		return "login";
	}

	@GetMapping("/user/list")
	public String listUsers(Model model, Pageable pageable,
			@RequestParam(value = "", required = false) String searchText,
			Principal principal) {
		logService.info(principal.getName() + " lists users");
		User user = usersService.getUserByEmail(principal.getName());
		Page<User> page = getUsers(pageable, searchText, user);
		List<User> list = page.getContent();
		for (User u : list) {
			u.setReceiveRequest(requestsService
					.findBySenderIdAndReceiverId(user.getId(), u.getId()));
		}
		model.addAttribute("usersList", list);
		model.addAttribute("page", page);
		return "/users/list";
	}

	@GetMapping("/admin/list")
	public String listUserAdmin(Model model, Principal principal,
			Pageable pageable,
			@RequestParam(value = "", required = false) String searchText) {
		logService.info("Admin " + principal.getName() + " "
				+ "lista los usuarios");
		User user = usersService.getUserByEmail(principal.getName());
		Page<User> page = getUsers(pageable, searchText, user);
		List<User> list = page.getContent();
		model.addAttribute("usersList", list);
		model.addAttribute("page", page);
		return "/users/adminlist";
	}

	@GetMapping("/admin/login")
	public String adminLoginGet() {
		logService.info("User tries to login as admin");
		return "/adminLogin";
	}

	@PostMapping("/admin/login")
	public String adminLoginPost(@Validated User user, Model model, HttpServletRequest request, HttpServletResponse response) {
		User user1 = usersService.getUserByEmail(user.getEmail());
		if (user1 == null) {
			logService.info("User " + user.getEmail() + " does not exist");
			model.addAttribute("noExist", "noExist");
			return "/adminLogin";
		}
		if (!user1.getRole().equals(rolesService.getAdmin())) {
			logService.info("User " + user.getEmail() + " is not an Admin");
			model.addAttribute("noAdmin", "noAdmin");
			return "/adminLogin";
		}
		if (!bCryptPasswordEncoder.matches(user.getPassword(),
				user1.getPassword())) {
			logService.info("User " + user.getEmail() + " is not an Admin");
			model.addAttribute("password", "password");
			return "/adminLogin";
		}
		securityService.autoLogin(user.getEmail(), user.getPassword(), request, response);
		logService.info(
				"Usuario " + user.getEmail() + " se ha logueado como Admin");
		return "redirect:/home";
	}

	@GetMapping("/home")
	public String home(Model model, Principal principal) {
		logService.info(principal.getName() + " logged in successfully");
		return "/home";
	}

	private Page<User> getUsers(Pageable pageable, String searchText,
			User user) {
		if (searchText != null && !searchText.isEmpty()) {
			return usersService.searchByEmailAndNameAndSurname(pageable,
					searchText, user.getId());
		}
		return usersService.getUsers(pageable, user.getId());
	}

	@PostMapping("/user/delete/{id}")
	public String deleteUser(Principal principal, @PathVariable Long id) {
		usersService.delete(id);
		logService.info("Admin " + principal.getName()
				+ " elimino al usuario con id " + id);
		return "redirect:/admin/list";
	}

	@GetMapping("/friends")
	public String showFriends(Model model, Pageable pageable,
			Principal principal) {
		logService.info(principal.getName() + " lists their friendships");
		User user = usersService.getUserByEmail(principal.getName());
		Page<User> users = usersService.findAllFriendsById(pageable,
				user.getId());
		model.addAttribute("usersList", users.getContent());
		model.addAttribute("page", users);
		return "/users/friends";
	}

}



