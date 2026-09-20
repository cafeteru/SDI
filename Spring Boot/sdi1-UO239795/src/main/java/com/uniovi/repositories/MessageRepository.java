package com.uniovi.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.uniovi.entities.Message;
import com.uniovi.entities.User;

public interface MessageRepository extends CrudRepository<Message, Long> {

	List<Message> findBySenderAndReceiverOrReceiverAndSenderOrderByDateAsc(User sender1, User receiver1, User receiver2, User sender2);
	
	Long countByReceiverAndSenderAndIsReadFalse(User receiver, User sender);

}
