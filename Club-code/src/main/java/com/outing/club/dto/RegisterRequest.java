package com.outing.club.dto;

public class RegisterRequest {
    private String name;
    private String emailId;
    private String password;
private String surname;
    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

	public String getSurname() {
		// TODO Auto-generated method stub
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
}
