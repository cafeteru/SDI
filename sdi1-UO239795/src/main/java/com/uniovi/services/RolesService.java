package com.uniovi.services;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

@Service
public class RolesService {
	private Map<String, String> roles = new HashMap<String, String>();

	@PostConstruct
	public void init() {
		roles.put("user", "ROLE_PUBLIC");
		roles.put("admin", "ROLE_ADMIN");
	}

	public String getUser() {
		return roles.get("user");
	}

	public String getAdmin() {
		return roles.get("admin");
	}
}
