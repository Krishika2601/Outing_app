package com.outing.club;

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
public class MainController {

	@Autowired
	private ClubMemberRepository memberRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OutingRepository outingRepository;

	public MainController(ClubMemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping({ "/" })
	public String visitors(@RequestParam(value = "name", defaultValue = "World", required = true) String name,
			Model model) {
		List<Category> categoriesList = categoryRepository.findAll();
		List<Outing> outingsList = outingRepository.findAll();
		model.addAttribute("categories", categoriesList);
		model.addAttribute("outings", outingsList);

		model.addAttribute("name", name);
		return "visitors";
	}

	@GetMapping({

			"/register" })
	public String showRegisterForm(Model model) {
		model.addAttribute("registerRequest", new RegisterRequest());
		return "register";
	}

	@GetMapping("/login")
	public String showLoginForm(Model model) {
		model.addAttribute("loginRequest", new LoginRequest());
		return "login";
	}

	@PostMapping("/register")
	public String registerMember(@ModelAttribute("registerRequest") RegisterRequest registerRequest) {
		ClubMember member = new ClubMember();
		member.setName(registerRequest.getName());
		member.setEmailId(registerRequest.getEmailId());
		member.setSurname(registerRequest.getSurname());
		member.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		memberRepository.save(member);
		return "redirect:/login";
	}

	@PostMapping("/login")
	public String memberLogin(@ModelAttribute("loginRequest") LoginRequest loginRequest, HttpServletResponse response,
			Model model) {
		ClubMember member = memberRepository.findByEmailId(loginRequest.getEmailId());

		if (member != null && passwordEncoder.matches(loginRequest.getPassword(), member.getPassword())) {
			String token = generateToken(member);
			member.setJwtToken(token);
			memberRepository.save(member);
			response.setHeader("Authorization", "Bearer " + token);
			model.addAttribute("loggedInMember", member);
			model.addAttribute("categories", categoryRepository.findAll());
			model.addAttribute("outings", outingRepository.findAll());
			model.addAttribute("members", memberRepository.findAll());

			return "dashboard"; 
		} else {
			model.addAttribute("error", "Invalid credentials");
			return "login"; 
		}
	}

	@GetMapping("/reset-password")
	public String showResetPasswordForm(Model model) {
		model.addAttribute("resetPasswordRequest", new PasswordResetRequest());
		return "reset-password";
	}

	@PostMapping("/reset-password")
	public String resetPassword(@ModelAttribute("resetPasswordRequest") PasswordResetRequest resetRequest) {
		ClubMember member = memberRepository.findByEmailId(resetRequest.getEmailId());

		if (member != null && passwordEncoder.matches(resetRequest.getCurrentPassword(), member.getPassword())) {
			member.setPassword(passwordEncoder.encode(resetRequest.getNewPassword()));
			memberRepository.save(member);
			return "redirect:/login?resetSuccess";
		} else {
			return "redirect:/reset-password?error";
		}
	}

	private String generateToken(ClubMember member) {
		@SuppressWarnings("deprecation")
		String token = Jwts.builder().setSubject(member.getEmailId()).claim("customerName", member.getName())
				.claim("Surname", member.getSurname()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(SignatureAlgorithm.HS512,
						"cd8b9a95db1f6e390ff0c31a95bc6ebf5274fc21c5e8acce403474a5f23b9510e2f4ad763cf9d49481e3f514c296d35a6b503bd6e6ffb3242b64ad9a5c6c9a25")
				.compact();
		return token;
	}

	@GetMapping("/dashboard")
	public String showDashboard(HttpServletRequest request, Model model) {
		// Extract JWT token from the URL
		String jwtToken = request.getParameter("jwtToken");
		System.out.println("Token: " + jwtToken);
		if (jwtToken != null) {
			ClubMember loggedInMember = extractMemberFromToken(jwtToken);
			if (loggedInMember != null) {
				model.addAttribute("loggedInMember", loggedInMember);
				model.addAttribute("categories", categoryRepository.findAll());
				model.addAttribute("outings", outingRepository.findAll());
				model.addAttribute("members", memberRepository.findAll());
				List<Outing> outings = outingRepository.findAll();
				outings.forEach(outing -> System.out.println(outing.getCreatedBy().getName()));
				model.addAttribute("outings", outings);

				model.addAttribute("members", memberRepository.findAll());
		
				return "dashboard"; 
			}
		}
	
		return "redirect:/login?error";
	}

// Method to extract JWT token from request header
	private String extractTokenFromRequest(HttpServletRequest request) {
		
		String authorizationHeader = request.getHeader("Authorization");
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			return authorizationHeader.substring(7); // Extract token after "Bearer "
		}
		return null;
	}

	@DeleteMapping("/logout")
	public ResponseEntity<String> logoutUser(@RequestParam(required = false) String memberId,
			@RequestParam(required = false) String memberName, HttpServletRequest request,
			HttpServletResponse response) {
		// Find the member based on ID or name
		ClubMember member = null;
		if (memberId != null && !memberId.isEmpty()) {
			// Find member by ID
			Optional<ClubMember> memberOptional = memberRepository.findById(Long.parseLong(memberId));
			if (memberOptional.isPresent()) {
				member = memberOptional.get();
			}
		} else if (memberName != null && !memberName.isEmpty()) {
			// Find member by name
			member = memberRepository.findByName(memberName);
		} else {
			return ResponseEntity.badRequest().body("Invalid member information");
		}

		if (member != null) {
			// Invalidate the session to clear the JWT token
			HttpSession session = request.getSession(false);
			if (session != null) {
				session.invalidate();
			}
			member.setJwtToken(null);
			memberRepository.save(member);

			// Clear the JWT token from the response header
			response.setHeader("Authorization", "");

			// Return a success message or appropriate response
			return ResponseEntity.ok("Logged out successfully for member: " + member.getName());
		} else {
			// Member not found, return an error response
			return ResponseEntity.badRequest().body("Member not found");
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