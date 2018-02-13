package com.uniovi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniovi.entities.Mark;
import com.uniovi.servicies.MarksService;

// Esta etiqueta no busca una plantilla.
@Controller
public class MarkController {

	@Autowired // Inyectar el servicio
	private MarksService marksService;

	@RequestMapping("/mark/list")
	public String getList(Model model) {
		model.addAttribute("markList", marksService.getMarks());
		return "mark/list";
	}

	@GetMapping("/mark/add")
	public String addMarkWeb(@ModelAttribute Mark mark) {
		return "/mark/add";
	}

	@PostMapping("/mark/add")
	public String addMarkPeticion(@ModelAttribute Mark mark) {
		marksService.addMark(mark);
		return "redirect:/mark/list";
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
	public String getDetail2(Model model, @PathVariable Long id) {
		model.addAttribute("mark", marksService.getMark(id));
		return "mark/details";
	}

	@RequestMapping("/mark/delete/{id}")
	public String deleteMark(@PathVariable Long id) {
		marksService.deleteMark(id);
		return "redirect:/mark/list";
	}

	@RequestMapping(value = "/mark/edit/{id}")
	public String getEdit(Model model, @PathVariable Long id) {
		model.addAttribute("mark", marksService.getMark(id));
		return "mark/edit";
	}

	@PostMapping(value = "/mark/edit/{id}")
	public String setEdit(Model model, @PathVariable Long id,
			@ModelAttribute Mark mark) {
		mark.setId(id);
		marksService.addMark(mark);
		return "redirect:/mark/details/" + id;
	}

	@GetMapping(value = "mark/filter")
	public String setFilter(Model model,
		@RequestParam(value = "description", required = false) String description) {
		List<Mark> a = marksService.findByDescripcion(description);
		model.addAttribute("markList", a);
		return "mark/filter";
	}

}
