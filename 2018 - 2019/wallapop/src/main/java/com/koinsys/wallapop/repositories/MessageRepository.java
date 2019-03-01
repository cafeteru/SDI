package com.koinsys.wallapop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.koinsys.wallapop.entities.Message;

public interface MessageRepository extends JpaRepository<Message, Long> {

}
