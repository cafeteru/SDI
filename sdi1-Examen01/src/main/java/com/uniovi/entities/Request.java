package com.uniovi.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.uniovi.entities.types.ResquestStatus;

@Entity
@Table(name = "TREQUEST")
public class Request {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private User sender;

	@ManyToOne
	private User receiver;

	@Enumerated(EnumType.STRING)
	private ResquestStatus status;

	public Request() {
	}

	public Request(User sender, User receiver) {
		this.sender = sender;
		this.receiver = receiver;
		status = ResquestStatus.SENT;
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

	public Long getId() {
		return id;
	}

	public ResquestStatus getStatus() {
		return status;
	}

	public void setStatus(ResquestStatus status) {
		this.status = status;
	}

	public void accept() {
		setStatus(ResquestStatus.ACCEPTED);
	}

	public void block() {
		setStatus(ResquestStatus.BLOCKED);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((receiver == null) ? 0 : receiver.hashCode());
		result = prime * result + ((sender == null) ? 0 : sender.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		if (receiver == null) {
			if (other.receiver != null)
				return false;
		} else if (!receiver.equals(other.receiver))
			return false;
		if (sender == null) {
			if (other.sender != null)
				return false;
		} else if (!sender.equals(other.sender))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", sender=" + sender + ", receiver="
				+ receiver + ", accepted=" + status + "]";
	}

}
