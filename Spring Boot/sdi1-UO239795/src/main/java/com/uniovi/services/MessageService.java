package com.uniovi.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uniovi.entities.Message;
import com.uniovi.entities.User;
import com.uniovi.repositories.MessageRepository;

@Service
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	public List<Message> getConversation(User u1, User u2, String search) {
		List<Message> allMessages = messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByDateAsc(u1, u2, u1, u2);
		if (Objects.nonNull(search) && !search.isEmpty()) {
			String searchLower = search.toLowerCase();
			return allMessages.stream()
				.filter(m -> m.getText() != null && m.getText().toLowerCase().contains(searchLower))
				.toList();
		}
		return allMessages;
	}

	public Message sendMessage(User sender, User receiver, String text, String imgUrl) {
		Message message = new Message(sender, receiver, text);
		message.setImg(imgUrl);
		return messageRepository.save(message);
	}

	public Long getUnreadMessagesCount(User receiver, User sender) {
		return messageRepository.countByReceiverAndSenderAndIsReadFalse(receiver, sender);
	}

	public void markConversationAsRead(User receiver, User sender) {
		List<Message> conversation = getConversation(receiver, sender, null);
		if (Objects.nonNull(conversation)) {
			for (Message m : conversation) {
				if (m.getReceiver().equals(receiver) && m.getSender().equals(sender) && !m.getIsRead()) {
					m.setIsRead(true);
					messageRepository.save(m);
				}
			}
		}
	}
}
