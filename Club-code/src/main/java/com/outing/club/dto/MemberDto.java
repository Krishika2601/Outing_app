package com.outing.club.dto;


import com.outing.club.entity.ClubMember;

public class MemberDto {
    private Long id;
    private String name;
    private String surname;
    private String emailId;

    public MemberDto() {
        // Default constructor
    }

    public MemberDto(ClubMember member) {
        this.id = member.getId();
        this.name = member.getName();
        this.surname = member.getSurname();
        this.emailId = member.getEmailId();
    }

    // Getters and setters
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}

