package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uniovi.entities.Mark;
import com.uniovi.servicies.MarksService;

// Esta etiqueta no busca una plantilla.
@RestController
public class MarkController {
	
	@Autowired // Inyectar el servicio
	private MarksService marksService;

	@GetMapping("/mark/list")
	public String getList() {
		return marksService.getMarks().toString();
	}

	@RequestMapping(value = "/mark/add", method = RequestMethod.POST)
	public String setMark(@ModelAttribute Mark mark) {
		marksService.addMark(mark);
		return "Ok";
	}

	/**
	 * La URL puede recibir un parámetro con clave “id”, no tiene porque ir en
	 * orden. Aqui hay que indicar que es cada parámetro
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/mark/details")
	public String getDetail(@RequestParam Long id) {
		return " Getting Detail: " + id;
	}

	/**
	 * Parámetros GET incluidos en el propio Path. Aquí sabe el primer parámetro
	 * será siempre el id.
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/mark/details/{id}")
	public String getDetail2(@PathVariable Long id) {
		return marksService.getMark(id).toString();
	}

	@RequestMapping("/mark/delete/{id}")
	public String deleteMark(@PathVariable Long id) {
		marksService.deleteMark(id);
		return "Ok";
	}

}
