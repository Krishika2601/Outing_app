package com.outing.club.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.outing.club.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	Optional<Category> findByName(String categoryName);
    // Additional custom query methods if needed
}