package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.uniovi.entities.Request;
import com.uniovi.entities.User;
import com.uniovi.services.RequestsService;
import com.uniovi.services.UsersService;
import com.uniovi.services.util.UtilService;

@Controller
public class RequestController {
	@Autowired
	private UtilService utilService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private RequestsService requestsService;

	@GetMapping("/request/send/{id}")
	public String deleteMark(@PathVariable Long id) {
		User user = utilService.getCurrentUser();
		requestsService.add(new Request(user, usersService.getUser(id)));
		return "redirect:/user/list";
	}
}
