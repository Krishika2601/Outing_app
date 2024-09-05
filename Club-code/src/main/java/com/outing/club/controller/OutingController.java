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
public class OutingController {

	@Autowired
	private ClubMemberRepository memberRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OutingRepository outingRepository;

	public OutingController(ClubMemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

// Get details of an outing
	@GetMapping("/outings/{outingId}")
	public Outing getOutingDetails(@PathVariable Long outingId) {
		return outingRepository.findById(outingId).orElse(null);
	}

// Search outings based on criteria
	@GetMapping("/outings/search")
	public List<Outing> searchOutings(@RequestParam(name = "keyword") String keyword) {
		// Implement search logic based on keyword
		return outingRepository.findByNameContainingIgnoreCase(keyword);
	}

	@GetMapping("/createOuting")
	public String showCreateOutingForm(Model model, HttpServletRequest request) {
		Iterable<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);
		String jwtToken = request.getParameter("jwtToken");
		System.out.println("JWT Token received: " + jwtToken);
		ClubMember loggedInMember = extractMemberFromToken(jwtToken);
		model.addAttribute("loggedInMember", loggedInMember);
		return "createOuting"; 
	}

	@PostMapping("/outings")
	public String createOuting(@ModelAttribute Outing outing, @RequestParam("memberName") String memberName,
			HttpServletRequest request, Model model) {
		// Extract member from JWT token
		String jwtToken = request.getParameter("jwtToken");
		System.out.println("JWT Token received: " + jwtToken);
		ClubMember loggedInMember = extractMemberFromToken(jwtToken);

		model.addAttribute("loggedInMember", loggedInMember);
		if (loggedInMember != null) {
			// Search for the member by name in the database
			ClubMember createdByMember = memberRepository.findByName(memberName);
			if (createdByMember == null) {
				
				return "error-page"; 
			}
			outing.setCreatedBy(createdByMember);
			// Save the outing with the updated createdBy field
			outingRepository.save(outing);
			System.out.println("Updated Outing: " + outing.getName() + " (" + outing.getDescription() + ")");
			System.out.println("Done for modification in Outing");
			String token = jwtToken;

			// Add necessary attributes to the model for the dashboard
			model.addAttribute("loggedInMember", createdByMember);
			model.addAttribute("categories", categoryRepository.findAll());
			model.addAttribute("outings", outingRepository.findAll());
			model.addAttribute("members", memberRepository.findAll());
			return "redirect:/dashboard?jwtToken=" + jwtToken;
		} else {
			// Handle unauthorized access or invalid token
			return "redirect:/login?error";
		}
	}

	@PostMapping("/outing/{outingId}")
	public String deleteOutingr(@PathVariable Long outingId, HttpServletRequest request, Model model) {
		// Check if the member exists

		Optional<Outing> outingOptional = outingRepository.findById(outingId);
		if (outingOptional.isPresent()) {
			// Delete the member
			outingRepository.deleteById(outingId);
			String jwtToken = request.getParameter("jwtToken");
			ClubMember loggedInMember = extractMemberFromToken(jwtToken);
			model.addAttribute("loggedInMember", loggedInMember);
			model.addAttribute("categories", categoryRepository.findAll());
			model.addAttribute("outings", outingRepository.findAll());
			model.addAttribute("members", memberRepository.findAll());
			return "redirect:/dashboard?jwtToken=" + jwtToken;
		} else {
			// Handle member not found
			return "error-page"; // Redirect to an error page or handle as needed
		}
	}

// Modify an outing
	@GetMapping("/outings/modify/{outingId}")
	public String getModifyOutingForm(@PathVariable Long outingId, Model model, HttpServletRequest request) {
		// Retrieve the outing details by ID from the repository
		Outing outing = outingRepository.findById(outingId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid outing ID: " + outingId));

		// Add the outing object to the model to pass it to the view
		model.addAttribute("outing", outing);
		List<Category> categories = categoryRepository.findAll();
		model.addAttribute("categories", categories);

		// Extract JWT token from request
		String jwtToken = request.getParameter("jwtToken");
		System.out.println("JWT Token received: " + jwtToken);
		ClubMember loggedInMember = extractMemberFromToken(jwtToken);
		model.addAttribute("loggedInMember", loggedInMember);
		// Extract member from JWT token
		if (loggedInMember != null) {
			System.out.println(
					"Logged-in Member: " + loggedInMember.getName() + " (" + loggedInMember.getEmailId() + ")");
			model.addAttribute("jwtToken", jwtToken);
			return "modifyOuting"; 
		} else {
		
			return "error-page"; 
		}
	}

	@PostMapping("/outings/{outingId}")
	public String modifyOuting(Model model, @PathVariable Long outingId, @RequestParam String name,
			@RequestParam String description, @RequestParam String website, @RequestParam LocalDateTime outingDate,
			@RequestParam Long categoryId, HttpServletRequest request, @RequestParam("memberName") String memberName) {

		// Extract JWT token from request
		String jwtToken = request.getParameter("jwtToken");
		System.out.println("JWT Token received: " + jwtToken);

		// Extract member from JWT token
		ClubMember loggedInMember = extractMemberFromToken(jwtToken);

		model.addAttribute("loggedInMember", loggedInMember);
		if (loggedInMember != null) {
			ClubMember createdByMember = memberRepository.findByName(memberName);
			System.out.println(
					"Logged-in Member: " + loggedInMember.getName() + " (" + loggedInMember.getEmailId() + ")");

			Optional<Outing> outingOptional = outingRepository.findById(outingId);
			if (outingOptional.isPresent()) {
				Outing outing = outingOptional.get();
				// Update outing details based on form parameters
				outing.setName(name);
				outing.setDescription(description);
				outing.setWebsite(website);
				outing.setCreatedBy(createdByMember);
				outing.setOutingDate(outingDate);
		
				Category category = categoryRepository.findById(categoryId)
						.orElseThrow(() -> new IllegalArgumentException("Invalid category ID: " + categoryId));
				outing.setCategory(category);
				outingRepository.save(outing);

				System.out.println("Updated Outing: " + outing.getName() + " (" + outing.getDescription() + ")");
				System.out.println("Done for modification in Outing");
				String token = jwtToken;

				model.addAttribute("loggedInMember", createdByMember);
				model.addAttribute("categories", categoryRepository.findAll());
				model.addAttribute("outings", outingRepository.findAll());
				model.addAttribute("members", memberRepository.findAll());
				return "redirect:/dashboard?jwtToken=" + jwtToken;
			} else {
				System.out.println("Outing not found for ID: " + outingId);
			}
		} else {
			System.out.println("Logged-in member not found or invalid token");
		}
		
		return "error-page"; 
	}

// Delete an outing
	@DeleteMapping("/outings/{outingId}")
	public void deleteOuting(@PathVariable Long outingId) {
		outingRepository.deleteById(outingId);
	}

	
	
	private String extractTokenFromRequest(HttpServletRequest request) {
		
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7); // Extract token after "Bearer "
		}
		return null;
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