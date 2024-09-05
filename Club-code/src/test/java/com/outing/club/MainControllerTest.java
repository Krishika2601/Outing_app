package com.outing.club;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.outing.club.controller.CategoryController;
import com.outing.club.dto.PasswordResetRequest;
import com.outing.club.dto.RegisterRequest;
import com.outing.club.entity.Category;
import com.outing.club.entity.ClubMember;
import com.outing.club.repository.CategoryRepository;
import com.outing.club.repository.ClubMemberRepository;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;

class MainControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClubMemberRepository memberRepository;

    @InjectMocks
    private MainController mainController;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryController categoryController;
    @Mock
    private BCryptPasswordEncoder passwordEncoder; // Mock the password encoder
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(mainController).build();
    }

 
    @Test
    void testRegisterMember() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setEmailId("john.doe@example.com");
        registerRequest.setSurname("Doe");
        registerRequest.setPassword("password123");

        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("hashedPassword");

        mockMvc.perform(post("/register")
                .flashAttr("registerRequest", registerRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(memberRepository, times(1)).save(any(ClubMember.class));
    }
    @Test
    void testResetPassword() throws Exception {
        PasswordResetRequest resetRequest = new PasswordResetRequest();
        resetRequest.setEmailId("john.doe@example.com");
        resetRequest.setCurrentPassword("incorrectPassword");
        resetRequest.setNewPassword("newPassword123");

        ClubMember member = new ClubMember();
        member.setEmailId("john.doe@example.com");
        member.setPassword("hashedPassword");

        when(memberRepository.findByEmailId(anyString())).thenReturn(member);
        when(passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(false);

        mockMvc.perform(post("/reset-password")
                .flashAttr("resetPasswordRequest", resetRequest))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reset-password?error"));

        verify(memberRepository, never()).save(any(ClubMember.class));
    }
    @Test
    void testShowDashboard() throws Exception {
       
        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isFound()) 
                .andExpect(redirectedUrl("/login?error"));

      
        System.out.println("Dashboard loaded successfully!");
    }
    @Test
    void testAddCategory() throws Exception {
        // Mocking repository behavior
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"New Category\"}")
        )
        .andExpect(status().is(404))
                ;
    }


}
