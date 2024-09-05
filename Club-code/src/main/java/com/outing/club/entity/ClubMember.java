package com.outing.club.entity;



import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class ClubMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String emailId;
    private String password;
    @OneToMany(mappedBy = "createdBy", fetch = FetchType.EAGER)
    private List<Outing> outings;
 public List<Outing> getOutings() {
		return outings;
	}
	public void setOutings(List<Outing> outings) {
		this.outings = outings;
	}
private String jwtToken; // Field to store JWT token
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public ClubMember() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ClubMember(Long id, String name, String surname, String emailId, String password) {
		super();
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.emailId = emailId;
		this.password = password;
	}
	
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getJwtToken() {
		return jwtToken;
	}
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}

    // Constructors, getters, and setters
}
