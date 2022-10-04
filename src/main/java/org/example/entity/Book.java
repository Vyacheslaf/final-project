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
	private int available;

	public static class Builder {

		private int id;
		private String author;
		private String title;
		private String publication;
		private int publicationYear;
		private int quantity;
		private int available;
		
		public Builder setId(int id) {
			this.id = id;
			return this;
		}
		
		public Builder setAuthor(String author) {
			this.author = author;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public Builder setPublication(String publication) {
			this.publication = publication;
			return this;
		}

		public Builder setPublicationYear(int publicationYear) {
			this.publicationYear = publicationYear;
			return this;
		}

		public Builder setQuantity(int quantity) {
			this.quantity = quantity;
			return this;
		}
		
		public Builder setAvailable(int available) {
			this.available = available;
			return this;
		}
		
		public Book build() {
			return new Book(this);
		}
	}
	
	private Book(Builder builder) {
		id = builder.id;
		author = builder.author;
		title = builder.title;
		publication = builder.publication;
		publicationYear = builder.publicationYear;
		quantity = builder.quantity;
		available = builder.available;
	}
	
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

	public int getAvailable() {
		return available;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setAvailable(int available) {
		this.available = available;
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", author=" + author + ", title=" + title + ", publication=" + publication
				+ ", publicationYear=" + publicationYear + "]";
	}
	
}
