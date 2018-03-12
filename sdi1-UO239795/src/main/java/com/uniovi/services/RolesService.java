package com.uniovi.services;

import org.springframework.stereotype.Service;

@Service
public class RolesService {

	public String getUser() {
		return "ROLE_PUBLIC";
	}

	public String getAdmin() {
		return "ROLE_ADMIN";
	}
}
