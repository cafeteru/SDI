package com.uniovi.servicies;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Mark;
import com.uniovi.entities.User;
import com.uniovi.repositories.MarksRepository;

@Service
public class MarksService {

	@Autowired
	private MarksRepository marksRepository;

	@Autowired
	private HttpSession httpSession;

	public List<Mark> getMarks() {
		List<Mark> marks = new ArrayList<Mark>();
		marksRepository.findAll().forEach(marks::add);
		return marks;
	}

	@SuppressWarnings("unchecked")
	public Mark getMark(Long id) {
		Set<Mark> consultedList = (Set<Mark>) httpSession
				.getAttribute("consultedList");
		if (consultedList == null) {
			consultedList = new HashSet<Mark>();
		}
		Mark markObtained = marksRepository.findOne(id);
		consultedList.add(markObtained);
		httpSession.setAttribute("consultedList", consultedList);
		return markObtained;
	}

	public void addMark(Mark mark) {
		// Si en Id es null le asignamos el ultimo + 1 de la lista
		marksRepository.save(mark);
	}

	public void deleteMark(Long id) {
		marksRepository.delete(id);
	}

	public List<Mark> findByDescripcion(String descripcion) {
		return marksRepository.findByDescipcion(descripcion);
	}

	public void setMarkResend(boolean revised, Long id) {
		Authentication auth = SecurityContextHolder.getContext()
				.getAuthentication();
		String dni = auth.getName();
		Mark mark = marksRepository.findOne(id);
		if (mark.getUser().getDni().equals(dni)) {
			marksRepository.updateResend(revised, id);
		}
	}

	public List<Mark> getMarksForUser(User user) {
		List<Mark> marks = new ArrayList<Mark>();
		if (user.getRole().equals("ROLE_STUDENT")) {
			marks = marksRepository.findAllByUser(user);
		}
		if (user.getRole().equals("ROLE_PROFESSOR")) {
			marks = getMarks();
		}
		return marks;
	}

	public List<Mark> searchMarksByDescriptionAndNameForUser(String searchText,
			User user) {
		List<Mark> marks = new ArrayList<Mark>();
		searchText = "%" + searchText + "%";
		if (user.getRole().equals("ROLE_STUDENT")) {
			marks = marksRepository.searchByDescriptionNameAndUser(searchText,
					user);
		}
		if (user.getRole().equals("ROLE_PROFESSOR")) {
			marks = marksRepository.searchByDescriptionAndName(searchText);
		}
		return marks;
	}
}