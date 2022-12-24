package org.example.entity;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {
	
	private static final long serialVersionUID = 2555857962415202589L;
	private int id;
	private String author;
	private String title;
	private String publication;
	private int publicationYear;
	private int quantity;
	private int available;
	private String isbn;

	public static class Builder {

		private int id;
		private String author;
		private String title;
		private String publication;
		private int publicationYear;
		private int quantity;
		private int available;
		private String isbn;
		
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
		
		public Builder setISBN(String isbn) {
			this.isbn = isbn;
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
		isbn = builder.isbn;
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

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, isbn, publication, publicationYear, title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(author, other.author) && Objects.equals(isbn, other.isbn)
				&& Objects.equals(publication, other.publication) && publicationYear == other.publicationYear
				&& Objects.equals(title, other.title);
	}

	@Override
	public String toString() {
		return "Book [id=" + id + ", author=" + author + ", title=" + title + ", publication=" + publication
				+ ", publicationYear=" + publicationYear + "]";
	}
	
}
