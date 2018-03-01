package com.uniovi.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	private String surName;

	@NotNull
	private String password;

	@Transient
	private String passwordConfirm;

	private String role;

	@OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
	private Set<Request> sentRequest;

	@OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
	private Set<Request> receiveRequest = new HashSet<>();;

	@ManyToOne
	@JoinColumn(name = "FRIEND_ID")
	private User friend;

	@OneToMany(mappedBy = "friend", fetch = FetchType.LAZY)
	private Set<User> friends = new HashSet<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Post> posts = new HashSet<>();

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

	public Set<Request> getSentRequest() {
		return sentRequest;
	}

	public void setSentRequest(Set<Request> sentRequest) {
		this.sentRequest = sentRequest;
	}

	public Set<Request> getReceiveRequest() {
		return receiveRequest;
	}

	public void setReceiveRequest(Set<Request> receiveRequest) {
		this.receiveRequest = receiveRequest;
	}

	public User getFriend() {
		return friend;
	}

	public void setFriend(User friend) {
		this.friend = friend;
	}

	public Set<User> getFriends() {
		return friends;
	}

	public void setFriends(Set<User> friends) {
		this.friends = friends;
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
				+ ", surName=" + surName + ", role=" + role + ", sentRequest="
				+ sentRequest + ", receiveRequest=" + receiveRequest
				+ ", friend=" + friend + ", friends=" + friends + ", posts="
				+ posts + "]";
	}

}
