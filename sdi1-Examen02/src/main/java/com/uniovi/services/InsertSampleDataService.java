package com.uniovi.services;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Post;
import com.uniovi.entities.Request;
import com.uniovi.entities.User;

@Service
public class InsertSampleDataService {
	@Autowired
	private UsersService usersService;

	@Autowired
	private RolesService rolesService;

	private String[] nombres = { "Iván", "Valery", "Ana", "Lorena", "Raúl",
			"María", "Laura", "Cristina", "Marta", "Sara", "Andrea", "Ana",
			"Alba", "Paula", "Sandra", "Nerea", " David", "Alejandro", "Daniel",
			"Javier", "Sergio", "Adrián", "Carlos", "Pablo", "Álvaro", "Pablo",
			"Jorge", "Hugo", "Manuel", "Pedro", "Elena", "Jairo", "Irene",
			"Iris", "Iria", "Miriam", "Miguel", "Luís" };

	private String[] apellidos = { "Aguilar", "Alonso", "Álvarez", "Arias",
			"Benítez", "Blanco", "Blesa", "Bravo", "Caballero", "Cabrera",
			"Calvo", "Cambil", "Campos", "Cano", "Carmona", "Carrasco",
			"Castillo", "Castro", "Cortés", "Crespo", "Cruz", "Delgado", "Díaz",
			"Díez", "Domínguez", "Durán", "Esteban", "Fernández", "Ferrer",
			"Flores", "Fuentes", "Gallardo", "Gallego", "García", "Garrido",
			"Gil", "Giménez", "Gómez", "González", "Guerrero", "Gutiérrez",
			"Hernández", "Herrera", "Herrero", "Hidalgo", "Iglesias", "Jiménez",
			"León", "López", "Lorenzo", "Lozano", "Marín", "Márquez", "Martín",
			"Martínez", "Medina", "Méndez", "Molina", "Montero", "Montoro",
			"Mora", "Morales", "Moreno", "Moya", "Navarro", "Nieto", "Ortega",
			"Ortiz", "Parra", "Pascual", "Pastor", "Pérez", "Prieto", "Ramírez",
			"Ramos", "Rey", "Reyes", "Rodríguez", "Román", "Romero", "Rubio",
			"Ruiz", "Sáez", "Soler", "Santana", "Santiago", "Santos", "Sanz",
			"Serrano", "Sánchez", "Vidal", "Ortín", "Velasco" };

	private String[] correos = { "gmail.com", "outlook.es", "yahoo.es",
			"hotmail.com", "telecable.es", "uniovi.es" };

	Set<User> users = new HashSet<User>();

	private User user1;

	@PostConstruct
	public void init() {
		// inicializar(100);
	}

	protected void inicializar(int limite) {
		user1 = new User("ivangonzalezmahagamage@gmail.com", "Iván",
				"González Mahagamage");
		user1.setPassword("123456");
		user1.setRole(rolesService.getAdmin());
		Post post = new Post(user1, "Prueba1", "Contenido", "");
		user1.getPosts().add(post);
		usersService.add(user1);

		User user2 = new User("igm1990@hotmail.com", "Iván",
				"González Mahagamage");
		user2.setPassword("123456");
		user2.setRole(rolesService.getUser());
		usersService.add(user2);
		rellenarBaseDatos(limite);

	}

	protected void rellenarBaseDatos(int limite) {
		while (users.size() < limite) {
			createUser();
		}

		Iterator<User> a = users.iterator();
		while (a.hasNext()) {
			usersService.add(a.next());
		}
	}

	protected void createUser() {
		String name = nombres[integer(0, nombres.length)];
		String surName1 = apellidos[integer(0, apellidos.length)];
		String surName2 = apellidos[integer(0, apellidos.length)];
		String surName = surName1 + " " + surName2;
		String emailEnd = "@" + correos[integer(0, correos.length)];
		String email = limpiarAcentos(name + surName1 + surName2) + emailEnd;
		User user = new User(email.toLowerCase(), name, surName);
		user.setPassword("123456");
		user.setRole(rolesService.getUser());
		int i = integer(0, 20);
		if (i % 5 == 0) {
			user.getReceiveRequests().add(new Request(user1, user));
			user.getReceiveRequests().add(new Request(user, user1));
		} else if (i % 11 == 0) {
			Request a = new Request(user1, user);
			a.block();
			user.getReceiveRequests().add(a);
		}
		Post post = new Post(user, "Titulo1", "Contenido", "");
		user.getPosts().add(post);
		users.add(user);
	}

	protected Integer integer(int min, int max) {
		return (int) (new java.util.Random().nextFloat() * (max - min) + min);
	}

	protected String limpiarAcentos(String cadena) {
		String limpio = null;
		if (cadena != null) {
			String valor = cadena;
			valor = valor.toUpperCase();
			// Normalizar texto para eliminar acentos, dieresis, cedillas y
			// tildes
			limpio = Normalizer.normalize(valor, Normalizer.Form.NFD);
			// Quitar caracteres no ASCII excepto la enie, interrogacion que
			// abre, exclamacion que abre, grados, U con dieresis.
			limpio = limpio
					.replaceAll("[^\\p{ASCII}(N\u0303)(n\u0303)(\u00A1)(\u00BF)"
							+ "(\u00B0)(U\u0308)(u\u0308)]", "");
			// Regresar a la forma compuesta, para poder comparar la enie con la
			// tabla de valores
			limpio = Normalizer.normalize(limpio, Normalizer.Form.NFC);
		}
		return limpio;
	}
}