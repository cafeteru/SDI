package com.uniovi.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.uniovi.entities.Friendship;
import com.uniovi.entities.Request;
import com.uniovi.entities.User;
import com.uniovi.services.FriendshipService;
import com.uniovi.services.LogService;
import com.uniovi.services.RequestsService;
import com.uniovi.services.UsersService;

@Controller
public class RequestController {

	@Autowired
	private UsersService usersService;

	@Autowired
	private RequestsService requestsService;

	@Autowired
	private FriendshipService friendshipService;

	private LogService logService = new LogService(this);

	@GetMapping("/request/send/{id}")
	public String sendRequest(@PathVariable Long id, Principal principal) {
		User user = usersService.getUserByEmail(principal.getName());
		logService.info(principal.getName() + " envia una petición al usuario"
				+ user.getEmail());
		requestsService.add(new Request(user, usersService.getUser(id)));
		return "redirect:/user/list";
	}

	@GetMapping("/request/accepted/{id}")
	public String acceptedRequest(@PathVariable Long id, Principal principal) {
		User receiver = usersService.getUserByEmail(principal.getName());
		User sender = usersService.getUser(id);
		logService.info(principal.getName() + " acepto la amistad de "
				+ sender.getEmail());
		acceptBoth(receiver, sender);
		createFriendshipBoth(receiver, sender);
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
		if (request == null) {
			request = new Request(sender, receiver);
		}
		request.accept();
		requestsService.modify(request);
	}

	/**
	 * Crea la amistad entre dos usuarios
	 * 
	 * @param receiver
	 * @param sender
	 */
	private void createFriendshipBoth(User receiver, User sender) {
		createFriendship(receiver, sender);
		createFriendship(sender, receiver);
		usersService.modify(sender);
		usersService.modify(receiver);
	}

	private void createFriendship(User receiver, User sender) {
		Friendship friendship = new Friendship(receiver, sender);
		receiver.getFriends().add(friendship);
		sender.getiAmFriendOf().add(friendship);
		friendshipService.add(friendship);
	}

	@GetMapping("/request/blocked/{id}")
	public String blockedRequest(@PathVariable Long id, Principal principal) {
		User receiver = usersService.getUserByEmail(principal.getName());
		User sender = usersService.getUser(id);
		blockedBoth(receiver, sender);
		return "redirect:/requests";
	}

	/**
	 * Aceptar las solicitudes de ambos usarios, si las hay y además crea la
	 * amistad entre los usuarios
	 * 
	 * @param receiver
	 * @param sender
	 */
	private void blockedBoth(User receiver, User sender) {
		blockRequest(receiver, sender);
		blockRequest(sender, receiver);
	}

	/**
	 * Acepta una solicitud de amistad. Le cambia el estado a aceptada y la
	 * guarda en la base de datos
	 * 
	 * @param receiver
	 * @param sender
	 */
	private void blockRequest(User receiver, User sender) {
		Request request = requestsService
				.findBySenderIdAndReceiverId(sender.getId(), receiver.getId());
		if (request != null) {
			request = new Request(sender, receiver);
		}
		request.block();
		requestsService.modify(request);
	}

}
