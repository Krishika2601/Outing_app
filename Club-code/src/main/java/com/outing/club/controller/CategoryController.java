package com.outing.club.controller;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.outing.club.dto.CategoryDto;
import com.outing.club.dto.LoginRequest;
import com.outing.club.dto.MemberDto;
import com.outing.club.dto.PasswordResetRequest;
import com.outing.club.dto.RegisterRequest;
import com.outing.club.entity.Category;
import com.outing.club.entity.ClubMember;
import com.outing.club.entity.Outing;
import com.outing.club.repository.CategoryRepository;
import com.outing.club.repository.ClubMemberRepository;
import com.outing.club.repository.OutingRepository;
import com.outing.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class CategoryController {

	@Autowired
	private ClubMemberRepository memberRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OutingRepository outingRepository;

	public CategoryController(ClubMemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}


// Get the list of categories
	@GetMapping("/categories")
	public String getCategories(Model model) {
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		model.addAttribute("categories", categoryRepository.findAll());
		return "categories"; // Assuming "categories.jsp" is the name of your JSP file
	}

	@GetMapping("/categories/{categoryId}")
	@Transactional 
	public String getCategoryWithOutings(@PathVariable Long categoryId, Model model, HttpServletRequest request) {
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
		if (categoryOptional.isPresent()) {
			Category category = categoryOptional.get();
			// Force initialization of the outings collection within the transaction
			String jwtToken = request.getParameter("jwtToken");
			System.out.println("JWT Token received: " + jwtToken);
			ClubMember loggedInMember = extractMemberFromToken(jwtToken);
			model.addAttribute("loggedInMember", loggedInMember);
			category.getOutings().size(); 
			model.addAttribute("category", category);
			model.addAttribute("outings", category.getOutings());
			return "categoryDetails"; 
		}
	
		return "redirect:/categories?error=notfound";
	}

	@GetMapping("/create-category")
	public String showCreateCategoryForm(Model model, HttpServletRequest request) {
		String jwtToken = request.getParameter("jwtToken");
		System.out.println("JWT Token received: " + jwtToken);
		ClubMember loggedInMember = extractMemberFromToken(jwtToken);
		model.addAttribute("loggedInMember", loggedInMember);
		return "createCategory"; // Assuming "createCategory.jsp" is the name of your JSP file
	}

	@PostMapping("/categories")
	public String createCategory(@RequestParam("name") String name, HttpServletRequest request, Model model) {
		Category category = new Category();
		category.setName(name);
		categoryRepository.save(category);
		model.addAttribute("categories", categoryRepository.findAll());
		String jwtToken = request.getParameter("jwtToken");
		System.out.println("JWT Token received: " + jwtToken);
		ClubMember loggedInMember = extractMemberFromToken(jwtToken);

		model.addAttribute("loggedInMember", loggedInMember);

		System.out.println("Done for modification in Category");
		model.addAttribute("categories", categoryRepository.findAll());
		model.addAttribute("outings", outingRepository.findAll());
		model.addAttribute("members", memberRepository.findAll());
		return "redirect:/dashboard?jwtToken=" + jwtToken;
	}

	@PostMapping("/categories/{categoryId}")
	@Transactional
	public String deleteCategoryAndOutings(@PathVariable Long categoryId, Model model, HttpServletRequest request) {
		// Check if the category exists
		Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
		if (categoryOptional.isPresent()) {
			Category category = categoryOptional.get();
			// Delete the associated outings first

			outingRepository.deleteByCategoryId(categoryId);
			// Delete the category
			categoryRepository.deleteById(categoryId);
			// Redirect to dashboard after deletion
			model.addAttribute("categories", categoryRepository.findAll());
			String jwtToken = request.getParameter("jwtToken");
			System.out.println("JWT Token received: " + jwtToken);
			ClubMember loggedInMember = extractMemberFromToken(jwtToken);

			model.addAttribute("loggedInMember", loggedInMember);

			System.out.println("Done for modification in Category");
			String token = jwtToken;

			model.addAttribute("categories", categoryRepository.findAll());
			model.addAttribute("outings", outingRepository.findAll());
			model.addAttribute("members", memberRepository.findAll());
			return "redirect:/dashboard?jwtToken=" + jwtToken;
		} else {
			// Handle category not found
			return "error-page"; // Redirect to an error page 
		}
	}


// Method to extract member details from JWT token
	private ClubMember extractMemberFromToken(String jwtToken) {
		try {
		
			Claims claims = Jwts.parser().setSigningKey(
					"cd8b9a95db1f6e390ff0c31a95bc6ebf5274fc21c5e8acce403474a5f23b9510e2f4ad763cf9d49481e3f514c296d35a6b503bd6e6ffb3242b64ad9a5c6c9a25")																														// generation
					.parseClaimsJws(jwtToken).getBody();

			String emailId = claims.getSubject(); 
		
			ClubMember loggedInMember = memberRepository.findByEmailId(emailId);
			return loggedInMember;
		} catch (Exception e) {
			
			return null;
		}
	}

}