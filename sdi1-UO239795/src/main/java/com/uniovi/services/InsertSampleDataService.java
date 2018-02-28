package com.uniovi.services;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;

@Service
public class InsertSampleDataService {
	@Autowired
	private UsersService usersService;

	@Autowired
	private RolesService rolesService;

	private String[] nombres = { "Iván", "Valery", "Ana", "Lorena", "Raúl",
			" María", "Laura", "Cristina", "Marta", "Sara", "Andrea", "Ana",
			"Alba", "Paula", "Sandra", "Nerea", " David", "Alejandro", "Daniel",
			"Javier", "Sergio", "Adrián", "Carlos", "Pablo", "Álvaro", "Pablo",
			"Jorge", "Hugo", "Manuel", "Pedro", "Elena", "Jairo", "Irene",
			"Iris", "Iria", "Miriam", "Miguel" };

	private String[] apellidos = { "Aguilar", "Alonso", "Álvarez", "Arias",
			"Benítez", "Blanco", "Blesa", "Bravo", "Caballero", "Cabrera",
			"Calvo", "Cambil", "Campos", "Cano", "Carmona", "Carrasco",
			"Castillo", "Castro", "Cortés", "Crespo", "Cruz", "Delgado", "Díaz",
			"Díez", "Domínguez", "Durán", "Esteban", "Fernández", "Ferrer",
			"Flores", "Fuentes", "Gallardo", "Gallego", "García", "Garrido",
			"Gil", "Giménez", "Gómez", "González", "Guerrero", "Gutiérrez",
			"Hernández", "Herrera", "Herrero", "Hidalgo", "Ibáñez", "Iglesias",
			"Jiménez", "León", "López", "Lorenzo", "Lozano", "Marín", "Márquez",
			"Martín", "Martínez", "Medina", "Méndez", "Molina", "Montero",
			"Montoro", "Mora", "Morales", "Moreno", "Moya", "Muñoz", "Navarro",
			"Nieto", "Núñez", "Ortega", "Ortiz", "Parra", "Pascual", "Pastor",
			"Peña", "Pérez", "Prieto", "Ramírez", "Ramos", "Rey", "Reyes",
			"Rodríguez", "Román", "Romero", "Rubio", "Ruiz", "Sáez", "Sánchez",
			"Santana", "Santiago", "Santos", "Sanz", "Serrano", "Soler", "Soto",
			"Suárez", "Torres", "Vargas", "Vázquez", "Vega", "Velasco",
			"Vicente", "Vidal" };

	private String[] correos = { "gmail.com", "outlook.es", "yahoo.es",
			"hotmail.com", "telecable.es", "uniovi.es" };

	Set<User> users = new HashSet<User>();

	@PostConstruct
	public void init() {
		// inicializar(1_000);
	}

	private void inicializar(int limite) {
		User user1 = new User("ivangonzalezmahagamage@gmail.com", "Iván",
				"González Mahagamage");
		user1.setPassword("123456");
		user1.setRole(rolesService.getAdmin());
		usersService.add(user1);
		rellenarBaseDatos(limite);
	}

	private void rellenarBaseDatos(int limite) {
		while (users.size() < limite) {
			createUser();
		}

		Iterator<User> a = users.iterator();
		while (a.hasNext()) {
			usersService.add(a.next());
		}
	}

	private void createUser() {
		String name = nombres[integer(0, nombres.length)];
		String surName1 = apellidos[integer(0, apellidos.length)];
		String surName2 = apellidos[integer(0, apellidos.length)];
		String surName = surName1 + " " + surName2;
		String emailEnd = "@" + correos[integer(0, correos.length)];
		String email = name + surName1 + surName2 + emailEnd;
		User user = new User(email.toLowerCase(), name, surName);
		user.setPassword("123456");
		user.setRole(rolesService.getUser());
		users.add(user);
	}

	public Integer integer(int min, int max) {
		return (int) (new java.util.Random().nextFloat() * (max - min) + min);
	}
}