package org.example.entity;

import java.io.Serializable;

public class Publication implements Serializable {
	
	private static final long serialVersionUID = 3182368804441753794L;
	private int id;
	private String publicationName;

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
