package org.example.entity;

public class Publication {
	public int id;
	public String publicationName;

	public int getId() {
		return id;
	}
	
	public String getPublicationName() {
		return publicationName;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setPublicationName(String publicationName) {
		this.publicationName = publicationName;
	}

	@Override
	public String toString() {
		return "Publication [id=" + id + ", publicationName=" + publicationName + "]";
	}
}
