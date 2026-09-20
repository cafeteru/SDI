package com.uniovi.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.uniovi.entities.Message;
import com.uniovi.entities.User;
import com.uniovi.services.MessageService;
import com.uniovi.services.PostService;
import com.uniovi.services.UsersService;
import com.uniovi.services.util.LogService;

@Controller
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UsersService usersService;
	
	@Autowired
	private PostService postService;

	private LogService logService = new LogService(this);

	@GetMapping("/chat/{id}")
	public String chat(Model model, Principal principal, @PathVariable Long id, 
			@RequestParam(value = "search", required = false) String search) {
		logService.info(principal.getName() + " opens chat with user " + id);
		User user = usersService.getUserByEmail(principal.getName());
		User friend = usersService.getUser(id);

		if (Objects.isNull(friend)) {
			return "redirect:/friends";
		}

		// Mark any unread messages from this friend as read
		messageService.markConversationAsRead(user, friend);

		List<Message> messages = messageService.getConversation(user, friend, search);

		model.addAttribute("messages", messages);
		model.addAttribute("friend", friend);
		model.addAttribute("user", user);
		model.addAttribute("search", search);

		return "/users/chat";
	}

	@PostMapping("/chat/{id}")
	public String sendMessage(Principal principal, @PathVariable Long id, 
			@RequestParam(value = "text", required = false) String text,
			@RequestParam(value = "imgn", required = false) MultipartFile img) {
		logService.info(principal.getName() + " sends message to user " + id);
		User user = usersService.getUserByEmail(principal.getName());
		User friend = usersService.getUser(id);

		if (Objects.nonNull(friend)) {
			String imgUrl = null;
			if (Objects.nonNull(img) && !img.isEmpty() && !img.getOriginalFilename().equals("")) {
				try {
					String fileName = postService.saveImg(img);
					imgUrl = "/imgUser/" + fileName;
				} catch (IOException e) {
					logService.error("Error saving image: " + e.getMessage());
				}
			}
			
			boolean hasText = Objects.nonNull(text) && !text.trim().isEmpty();
			boolean hasImg = Objects.nonNull(imgUrl);
			
			if (hasText || hasImg) {
				messageService.sendMessage(user, friend, hasText ? text : "", imgUrl);
			}
		}

		return "redirect:/chat/" + id;
	}
}
