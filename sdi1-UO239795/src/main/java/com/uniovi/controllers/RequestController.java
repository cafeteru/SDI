package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.uniovi.entities.Friendship;
import com.uniovi.entities.Request;
import com.uniovi.entities.User;
import com.uniovi.services.FriendshipService;
import com.uniovi.services.RequestsService;
import com.uniovi.services.UsersService;
import com.uniovi.services.util.UtilService;

@Controller
public class RequestController {
	@Autowired
	private UtilService utilService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private RequestsService requestsService;

	@Autowired
	private FriendshipService friendshipService;

	@GetMapping("/request/send/{id}")
	public String sendRequest(@PathVariable Long id) {
		User user = utilService.getCurrentUser();
		requestsService.add(new Request(user, usersService.getUser(id)));
		return "redirect:/user/list";
	}

	@GetMapping("/request/accepted/{id}")
	public String acceptedRequest(@PathVariable Long id) {
		User receiver = utilService.getCurrentUser();
		User sender = usersService.getUser(id);
		acceptBoth(receiver, sender);
		createFriendship(receiver, sender);
		return "redirect:/requests";
	}

	/**
	 * Aceptar las solicitudes de ambos usarios, si las hay y además crea la
	 * amistad entre los usuarios
	 * 
	 * @param receiver
	 * @param sender
	 */
	private void acceptBoth(User receiver, User sender) {
		acceptRequest(receiver, sender);
		acceptRequest(sender, receiver);
	}

	/**
	 * Acepta una solicitud de amistad. Le cambia el estado a aceptada y la
	 * guarda en la base de datos
	 * 
	 * @param receiver
	 * @param sender
	 */
	private void acceptRequest(User receiver, User sender) {
		Request request = requestsService
				.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
		if (request != null) {
			request.accept();
			requestsService.modify(request);
		}
	}

	/**
	 * Crea la amistad entre dos usuarios
	 * 
	 * @param receiver
	 * @param sender
	 */
	private void createFriendship(User receiver, User sender) {
		Friendship friendship = new Friendship(receiver, sender);
		receiver.getFriends().add(friendship);
		sender.getFriends().add(friendship);
		friendshipService.add(friendship);
		usersService.modify(sender);
		usersService.modify(receiver);
	}

}
