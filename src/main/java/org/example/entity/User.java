package org.example.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;

	private int id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String passportNumber;
	private UserRole role;
	private boolean blocked;
	private BigDecimal fine;
	
	public int getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public UserRole getRole() {
		return role;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public BigDecimal getFine() {
		return fine;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	public void setFine(BigDecimal fine) {
		this.fine = fine;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", role=" + role + "]";
	}
}
