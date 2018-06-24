package com.uniovi.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "TUSERS")
public class User {
	@Id
	@GeneratedValue
	private long id;

	@Column(unique = true)
	@NotNull
	private String email;

	private String name;

	@Column(name = "surname")
	private String surName;

	@NotNull
	private String password;

	@Transient
	private String passwordConfirm;

	private String role;

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
	private Set<Request> sentRequests = new HashSet<>();

	@OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
	private Set<Request> receiveRequests = new HashSet<>();

	@OneToMany(mappedBy = "friend")
	private Set<Friendship> friends = new HashSet<>();

	@OneToMany(mappedBy = "user")
	private Set<Friendship> iAmFriendOf = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Post> posts = new HashSet<>();

	// Esta variable se usa para mostrar el estado de la solicitud con el
	// usuario logeado a la hora de listar los usuarios en la aplicación
	@Transient
	private Request receiveRequest;

	public User() {
	}

	public User(String email, String name, String surName) {
		this.email = email;
		this.name = name;
		this.surName = surName;
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

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
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

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Set<Request> getSentRequests() {
		return sentRequests;
	}

	public void setSentRequests(Set<Request> sentRequests) {
		this.sentRequests = sentRequests;
	}

	public Set<Request> getReceiveRequests() {
		return receiveRequests;
	}

	public void setReceiveRequests(Set<Request> receiveRequests) {
		this.receiveRequests = receiveRequests;
	}

	public Request getReceiveRequest() {
		return receiveRequest;
	}

	public void setReceiveRequest(Request receiveRequest) {
		this.receiveRequest = receiveRequest;
	}

	public Set<Friendship> getFriends() {
		return friends;
	}

	public void setFriends(Set<Friendship> friends) {
		this.friends = friends;
	}

	public Set<Friendship> getiAmFriendOf() {
		return iAmFriendOf;
	}

	public void setiAmFriendOf(Set<Friendship> iAmFriendOf) {
		this.iAmFriendOf = iAmFriendOf;
	}

	public Set<Post> getPosts() {
		return posts;
	}

	public void setPosts(Set<Post> posts) {
		this.posts = posts;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", name=" + name
				+ ", surName=" + surName + ", role=" + role + "]";
	}

}
