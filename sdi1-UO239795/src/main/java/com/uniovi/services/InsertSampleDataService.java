package com.uniovi.services;

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

	@PostConstruct
	public void init() {
		User user1 = new User("ivangonzalezmahagamage@gmail.com", "Iván",
				"González Mahagamage");
		user1.setPassword("123456");
		user1.setRole(rolesService.getAdmin());
		usersService.add(user1);

	}
	
	private String[] nombres = { "MARIA CARMEN", "MARIA", "CARMEN", "JOSEFA",
			"ISABEL", "ANA MARIA", "MARIA PILAR", "MARIA DOLORES",
			"MARIA TERESA", "ANA", "LAURA", "FRANCISCA", "MARIA ANGELES",
			"CRISTINA", "ANTONIA", "MARTA", "DOLORES", "MARIA ISABEL",
			"MARIA JOSE", "LUCIA", "MARIA LUISA", "PILAR", "ELENA",
			"CONCEPCION", "ANTONIO", "JOSE", "MANUEL", "FRANCISCO", "JUAN",
			"DAVID", "JOSE ANTONIO", "JOSE LUIS", "JAVIER", "FRANCISCO JAVIER",
			"JESUS", "DANIEL", "CARLOS", "MIGUEL", "ALEJANDRO", "JOSE MANUEL",
			"RAFAEL", "PEDRO", "ANGEL", "MIGUEL ANGEL", "JOSE MARIA",
			"FERNANDO", "PABLO", "LUIS", "SERGIO", "JORGE", "ALBERTO",
			"JUAN CARLOS", "JUAN JOSE", "ALVARO", "DIEGO", "ADRIAN",
			"JUAN ANTONIO", "RAUL", "ENRIQUE", "RAMON", "VICENTE", "IVAN" };

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
}