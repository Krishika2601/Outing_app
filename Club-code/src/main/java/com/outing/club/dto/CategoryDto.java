package com.outing.club.dto;

import com.outing.club.entity.Category;
import com.outing.club.entity.Outing;

import java.util.List;

public class CategoryDto {
    private Long id;
    private String name;
    private List<Outing> outings;

    public CategoryDto(Category category, List<Outing> outings) {
        this.id = category.getId();
        this.name = category.getName();
        this.outings = outings;
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

    public List<Outing> getOutings() {
        return outings;
    }

    public void setOutings(List<Outing> outings) {
        this.outings = outings;
    }
}
