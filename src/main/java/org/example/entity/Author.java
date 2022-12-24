package org.example.entity;

import java.io.Serializable;

public class Author implements Serializable {

	private static final long serialVersionUID = -437840623341122308L;
	private int id;
	private String fullName;
	
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
