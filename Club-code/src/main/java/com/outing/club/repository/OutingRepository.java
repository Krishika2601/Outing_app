package com.outing.club.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.outing.club.entity.Category;
import com.outing.club.entity.ClubMember;
import com.outing.club.entity.Outing;

import java.util.List;

public interface OutingRepository extends JpaRepository<Outing, Long> {
    List<Outing> findByCategory(Category category);
    // Additional custom query methods if needed

	List<Outing> findByNameContainingIgnoreCase(String keyword);

	List<Outing> findByCreatedBy(ClubMember member);

	void deleteByCategory(Category category);

	void deleteByCategoryId(Long categoryId);

	void deleteByCreatedBy(ClubMember member);

}