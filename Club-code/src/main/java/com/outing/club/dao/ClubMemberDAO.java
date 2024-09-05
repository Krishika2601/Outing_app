package com.outing.club.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.outing.club.entity.ClubMember;
import com.outing.club.repository.ClubMemberRepository;

import java.util.List;
import java.util.Optional;

@Component
public class ClubMemberDAO {
    @Autowired
    private ClubMemberRepository repository;

    public List<ClubMember> getAllMembers() {
        return repository.findAll();
    }

    public Optional<ClubMember> getMemberById(Long id) {
        return repository.findById(id);
    }

    public ClubMember createOrUpdateMember(ClubMember member) {
        return repository.save(member);
    }

    public void deleteMember(Long id) {
        repository.deleteById(id);
    }
}
