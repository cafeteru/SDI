package com.uniovi.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uniovi.services.LogService;

@Controller
public class HomeController {

	private LogService logService = new LogService(this);

	@RequestMapping("/")
	public String index() {
		logService.info("Usuario ha entrado en la aplicación");
		return "index";
	}

}
