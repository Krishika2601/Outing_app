package com.outing.club.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.outing.club.entity.ClubMember;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    ClubMember findByEmailId(String emailAddress);

	boolean existsByEmailId(String emailId);

	ClubMember findByName(String memberName);
}