package com.koinsys.wallapop.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.koinsys.wallapop.entities.types.Rol;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String surname;

	@Column(unique = true)
	private String email;
	private String password;

	@Transient
	private String repassword;

	private double money;

	@Enumerated(EnumType.STRING)
	private Rol rol;

	private boolean active;

	@OneToMany(mappedBy = "owner")
	private Set<Sale> publishedSales = new HashSet<>();

	@OneToMany(mappedBy = "buyer")
	private Set<Sale> boughtSales = new HashSet<>();

	@OneToMany(mappedBy = "sender")
	private Set<Message> sentMessages = new HashSet<>();

	@OneToMany(mappedBy = "receiver")
	private Set<Message> receivedMessage = new HashSet<>();

	public User() {
		this.active = true;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Set<Sale> getPublishedSales() {
		return publishedSales;
	}

	public void setPublishedSales(Set<Sale> publishedSales) {
		this.publishedSales = publishedSales;
	}

	public Set<Sale> getBoughtSales() {
		return boughtSales;
	}

	public void setBoughtSales(Set<Sale> boughtSales) {
		this.boughtSales = boughtSales;
	}

	public Set<Message> getSentMessages() {
		return sentMessages;
	}

	public void setSentMessages(Set<Message> sentMessages) {
		this.sentMessages = sentMessages;
	}

	public Set<Message> getReceivedMessage() {
		return receivedMessage;
	}

	public void setReceivedMessage(Set<Message> receivedMessage) {
		this.receivedMessage = receivedMessage;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname + ", email=" + email + ", rol=" + rol
				+ ", active=" + active + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
