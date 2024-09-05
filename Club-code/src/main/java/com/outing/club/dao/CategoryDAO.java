package com.outing.club.dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.outing.club.entity.Category;
import com.outing.club.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Component
public class CategoryDAO {
    @Autowired
    private CategoryRepository repository;

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return repository.findById(id);
    }

    public Category createOrUpdateCategory(Category category) {
        return repository.save(category);
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }
}
