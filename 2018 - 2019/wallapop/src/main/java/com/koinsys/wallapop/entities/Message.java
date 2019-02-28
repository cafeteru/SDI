package com.koinsys.wallapop.entities;

import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User sender;

	@ManyToOne
	private User receiver;

	private String body;

	private LocalDateTime date;

	private boolean valid;

	public Message() {
	}

	public Long getId() {
		return id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Message message = (Message) o;
		return id.equals(message.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Message{" + "id=" + id + ", sender=" + sender + ", receiver="
				+ receiver + ", body='" + body + '\'' + ", date=" + date
				+ ", valid=" + valid + '}';
	}
}
