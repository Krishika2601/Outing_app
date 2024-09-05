package com.outing.club.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Outing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String website;
    private LocalDateTime outingDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private ClubMember createdBy;
    // Getter method for category
    public Category getCategory() {
        return category;
    }

    // Setter method for category
    public void setCategory(Category category) {
        this.category = category;
    }
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public LocalDateTime getOutingDate() {
		return outingDate;
	}

	public void setOutingDate(LocalDateTime outingDate) {
		this.outingDate = outingDate;
	}

	public ClubMember getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(ClubMember createdBy) {
		this.createdBy = createdBy;
	}
    

    // Constructors, getters, and setters
}
