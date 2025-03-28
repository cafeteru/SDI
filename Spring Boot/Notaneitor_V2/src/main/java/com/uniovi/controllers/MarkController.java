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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.uniovi.entities.Mark;
import com.uniovi.servicies.MarksService;
import com.uniovi.servicies.UsersService;

// Esta etiqueta no busca una plantilla.
@Controller
public class MarkController {

	@Autowired // Inyectar el servicio
	private MarksService marksService;

	@Autowired
	private UsersService usersService;

	@RequestMapping("/mark/list")
	public String getList(Model model) {
		model.addAttribute("markList", marksService.getMarks());
		return "mark/list";
	}

	@GetMapping("/mark/add")
	public String addMarkWeb(Model model) {
		model.addAttribute("usersList", usersService.getUsers());
		return "mark/add";
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

	@GetMapping(value = "mark/filter")
	public String setFilter(Model model,
			@RequestParam(value = "description", required = false) String description) {
		List<Mark> a = marksService.findByDescripcion(description);
		model.addAttribute("markList", a);
		return "mark/filter";
	}

	@RequestMapping("/mark/list/update")
	public String updateList(Model model) {
		model.addAttribute("markList", marksService.getMarks());
		return "mark/list :: tableMarks";
	}

	@RequestMapping(value = "/mark/edit/{id}")
	public String getEdit(Model model, @PathVariable Long id) {
		model.addAttribute("mark", marksService.getMark(id));
		model.addAttribute("usersList", usersService.getUsers());
		return "mark/edit";
	}

	@RequestMapping(value = "/mark/edit/{id}", method = RequestMethod.POST)
	public String setEdit(Model model, @PathVariable Long id,
			@ModelAttribute Mark mark) {
		Mark original = marksService.getMark(id);
		// modificar solo score y description
		original.setScore(mark.getScore());
		original.setDescription(mark.getDescription());
		marksService.addMark(original);
		return "redirect:/mark/details/" + id;
	}

}
