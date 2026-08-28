package com.uniovi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uniovi.services.util.LogService;

@Controller
public class HomeController {

	private LogService logService = new LogService(this);

	@RequestMapping("/")
	public String index() {
		logService.info("User has entered the application");
		return "index";
	}

}

