package com.koinsys.wallapop.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.joda.time.LocalDateTime;

import com.koinsys.wallapop.entities.types.SaleStatus;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String title;
	private String details;

	private LocalDateTime creationDate;

	private double price;

	@Enumerated(EnumType.STRING)
	private SaleStatus status;

	@ManyToOne
	private User owner;

	@ManyToOne
	private User buyer;

	@OneToMany(mappedBy = "sale")
	private Set<Message> messages = new HashSet<>();

	public Sale() {
		this.status = SaleStatus.ONSALE;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public SaleStatus getStatus() {
		return status;
	}

	public void setStatus(SaleStatus status) {
		this.status = status;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getBuyer() {
		return buyer;
	}

	public void setBuyer(User buyer) {
		this.buyer = buyer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Sale sale = (Sale) o;
		return Objects.equals(id, sale.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "Sale{" + "id=" + id + ", title='" + title + '\'' + ", details='"
				+ details + '\'' + ", creationDate=" + creationDate + ", price="
				+ price + ", status=" + status + ", owner=" + owner + ", buyer="
				+ buyer + '}';
	}
}
