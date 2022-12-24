package org.example.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Order implements Serializable {

	private static final long serialVersionUID = -6885569595941715374L;
	private int id;
	private User user;
	private Book book;
	private OrderState state;
	private LocalDateTime createTime;
	private LocalDateTime returnTime;
	private int fine;
	
	public int getId() {
		return id;
	}
	
	public OrderState getState() {
		return state;
	}
	
	public LocalDateTime getCreateTime() {
		return createTime;
	}
	
	public LocalDateTime getReturnTime() {
		return returnTime;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setState(OrderState state) {
		this.state = state;
	}
	
	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
	
	public void setReturnTime(LocalDateTime returnTime) {
		this.returnTime = returnTime;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public Book getBook() {
		return book;
	}
	
	public void setBook(Book book) {
		this.book = book;
	}
	
	public int getFine() {
		return fine;
	}
	
	public void setFine(int fine) {
		this.fine = fine;
	}

	@Override
	public int hashCode() {
		return Objects.hash(book, id, state, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Order other = (Order) obj;
		return Objects.equals(book, other.book) && id == other.id && state == other.state
				&& Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "Order [id=" + id + ", user=" + user + ", book=" + book + ", state=" + state + "]";
	}
}
