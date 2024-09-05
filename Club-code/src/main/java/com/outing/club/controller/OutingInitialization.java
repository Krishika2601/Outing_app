package com.outing.club.controller;

import com.outing.club.entity.Category;
import com.outing.club.entity.ClubMember;
import com.outing.club.entity.Outing;
import com.outing.club.repository.CategoryRepository;
import com.outing.club.repository.ClubMemberRepository;
import com.outing.club.repository.OutingRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class OutingInitialization {

    @Autowired
    private ClubMemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OutingRepository outingRepository;
    @PostConstruct
    public void initializeOutings() {
        // Create default member if not exists
        ClubMember defaultMember = memberRepository.findByName("Testdefault");
        if (defaultMember == null) {
            defaultMember = new ClubMember();
            defaultMember.setName("Testdefault");
            defaultMember.setSurname("Default");
            defaultMember.setEmailId("testdefault@example.com");
            defaultMember.setPassword(passwordEncoder.encode("TestDefault"));
            memberRepository.save(defaultMember);
        }

        // Create categories if not exists
        Category category1 = createCategoryIfNotExists("Category Initial1");
        Category category2 = createCategoryIfNotExists("Category Initial2");

        // Create outings and assign to default member
        List<Outing> outings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Outing outing = new Outing();
            outing.setName("Outing Initial" + (i + 1));
            outing.setDescription("Description for Outing Initial" + (i + 1));
            outing.setWebsite("www.outingtest" + (i + 1) + ".com");
            outing.setOutingDate(LocalDateTime.now().plusDays(i));

            if (i % 2 == 0) {
                outing.setCategory(category1);
            } else {
                outing.setCategory(category2);
            }

            outing.setCreatedBy(defaultMember);
            outings.add(outing);
        }

        // Save all outings
        outingRepository.saveAll(outings);
    }

    private Category createCategoryIfNotExists(String categoryName) {
        Optional<Category> categoryOptional = categoryRepository.findByName(categoryName);
        return categoryOptional.orElseGet(() -> {
            Category newCategory = new Category();
            newCategory.setName(categoryName);
            return categoryRepository.save(newCategory);
        });
    }
}
