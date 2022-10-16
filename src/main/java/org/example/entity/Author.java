package org.example.entity;

public class Author {

	public int id;
	public String fullName;
	
	public int getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	@Override
	public String toString() {
		return "Author [id=" + id + ", fullName=" + fullName + "]";
	}
}
