package org.example.entity;

import java.io.Serializable;

public class Book implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private int id;
	private String author;
	private String title;
	private String publication;
	private int publicationYear;
	private int quantity;
	private int restOf;

	public int getId() {
		return id;
	}

	public String getAuthor() {
		return author;
	}

	public String getTitle() {
		return title;
	}

	public String getPublication() {
		return publication;
	}

	public int getPublicationYear() {
		return publicationYear;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getRestOf() {
		return restOf;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPublication(String publication) {
		this.publication = publication;
	}

	public void setPublicationYear(int publicationYear) {
		this.publicationYear = publicationYear;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setRestOf(int restOf) {
		this.restOf = restOf;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", author=" + author + ", title=" + title + ", publication=" + publication
				+ ", publicationYear=" + publicationYear + "]";
	}
}
