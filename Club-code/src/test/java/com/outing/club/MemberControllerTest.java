package com.outing.club;

import java.util.Optional;


import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.outing.club.controller.MemberController;
import com.outing.club.dto.MemberDto;
import com.outing.club.entity.ClubMember;
import com.outing.club.repository.ClubMemberRepository;



@SpringBootTest
public class MemberControllerTest {

    @Mock
    private ClubMemberRepository memberRepository;

    @InjectMocks
    private MemberController memberController;

    @Test
    void testGetMembers() {
        // Arrange
        List<ClubMember> expectedMembers = Arrays.asList(
                new ClubMember(1L, "JohnDoe","surname", "john.doe@example.com","password"),
                new ClubMember(2L, "JaneDoe","surname", "jane.doe@example.com","password")
        );
        when(memberRepository.findAll()).thenReturn(expectedMembers);

        // Act
        List<ClubMember> actualMembers = memberController.getMembers();

        // Assert
        assertEquals(expectedMembers.size(), actualMembers.size());
        for (int i = 0; i < expectedMembers.size(); i++) {
            assertEquals(expectedMembers.get(i).getName(), actualMembers.get(i).getName());
            assertEquals(expectedMembers.get(i).getEmailId(), actualMembers.get(i).getEmailId());
        }
    }
    @Test
    void testModifyMember() {
        // Arrange
        long memberId = 1L;
        ClubMember originalMember = new ClubMember(memberId, "JohnDoe", "surname", "john.doe@example.com", "password");
        ClubMember modifiedMember = new ClubMember(memberId, "JaneDoe", "surname", "john.doe.jr@example.com", "password");
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(originalMember));
        when(memberRepository.save(originalMember)).thenReturn(modifiedMember);

        // Act
        ClubMember updatedMember = memberController.modifyMember(memberId, modifiedMember);

        // Assert
        assertEquals(modifiedMember.getId(), updatedMember.getId());
        assertEquals(modifiedMember.getName(), updatedMember.getName());
        assertEquals(modifiedMember.getEmailId(), updatedMember.getEmailId());
    }
    @Test
    void testGetMemberDetails() {
        // Arrange
        long memberId = 1L;
        ClubMember member = new ClubMember(memberId, "JohnDoe", "surname", "john.doe@example.com", "password");
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));

        // Act
        MemberDto memberDto = memberController.getMemberDetails(memberId);

        // Assert
        assertEquals(member.getId(), memberDto.getId());
        assertEquals(member.getName(), memberDto.getName());
        assertEquals(member.getSurname(), memberDto.getSurname());
        assertEquals(member.getEmailId(), memberDto.getEmailId());
    }
   
}
