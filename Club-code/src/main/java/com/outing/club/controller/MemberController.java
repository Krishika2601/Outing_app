package com.outing.club.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.outing.club.dto.MemberDto;
import com.outing.club.entity.ClubMember;
import com.outing.club.entity.Outing;
import com.outing.club.repository.CategoryRepository;
import com.outing.club.repository.ClubMemberRepository;
import com.outing.club.repository.OutingRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class MemberController {

	@Autowired
	private ClubMemberRepository memberRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private OutingRepository outingRepository;

	public MemberController(ClubMemberRepository memberRepository, BCryptPasswordEncoder passwordEncoder) {
		this.memberRepository = memberRepository;
		this.passwordEncoder = passwordEncoder;
	}

// Get the list of members
	@GetMapping("/members")
	public List<ClubMember> getMembers() {
		return memberRepository.findAll();
	}

	@GetMapping("/members/{memberId}/modify")
	public String getModifyMemberForm(@PathVariable Long memberId, Model model, HttpServletRequest request) {
		Optional<ClubMember> memberOptional = memberRepository.findById(memberId);
		if (memberOptional.isPresent()) {
			ClubMember member = memberOptional.get();
			model.addAttribute("member", member);
			model.addAttribute("modifiedMember", new ClubMember()); // Add the modifiedMember object to the model
			String jwtToken = request.getParameter("jwtToken");
			System.out.println("JWT Token received: " + jwtToken);
			ClubMember loggedInMember = extractMemberFromToken(jwtToken);

			model.addAttribute("loggedInMember", loggedInMember);
			return "modifyMember";
		} else {
			// Handle member not found
			return "error-page"; // Redirect to an error page or handle as needed
		}
	}

	@PostMapping("/members/{memberId}")
	@Transactional
	public String deleteMember(@PathVariable Long memberId, HttpServletRequest request, Model model) {
		// Check if the member exists
		Optional<ClubMember> memberOptional = memberRepository.findById(memberId);
		if (memberOptional.isPresent()) {
			// Fetch and delete the outings created by this member
			// Fetch and delete the outings created by this member
			ClubMember member = memberOptional.get();
			outingRepository.deleteByCreatedBy(member);
			memberRepository.deleteById(memberId);
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

	@PostMapping("/members/{memberId}/modify")
	public String modifyMember2(@PathVariable Long memberId, @ModelAttribute ClubMember modifiedMember,
			HttpServletRequest request, Model model) {
		Optional<ClubMember> memberOptional = memberRepository.findById(memberId);
		String jwtToken = request.getParameter("jwtToken");
		System.out.println("JWT Token received: " + jwtToken);
		ClubMember loggedInMember = extractMemberFromToken(jwtToken);

		model.addAttribute("loggedInMember", loggedInMember);
		if (memberOptional.isPresent()) {
			ClubMember member = memberOptional.get();
			// Update member details based on the modifiedMember object
			member.setName(modifiedMember.getName());
			member.setSurname(modifiedMember.getSurname());
			member.setEmailId(modifiedMember.getEmailId());
			memberRepository.save(member);

			System.out.println("JWT Token received: " + jwtToken);

			model.addAttribute("loggedInMember", loggedInMember);
			System.out.println("Done for modification in Category");
			model.addAttribute("categories", categoryRepository.findAll());
			model.addAttribute("outings", outingRepository.findAll());
			model.addAttribute("members", memberRepository.findAll());
			return "redirect:/dashboard?jwtToken=" + jwtToken;
		} else {
			// Handle member not found
			return "error-page"; // Redirect to an error page
		}
	}

// Get a specific member
	@GetMapping("/members/{memberId}")
	@Transactional
	public MemberDto getMemberDetails(@PathVariable Long memberId) {
		Optional<ClubMember> memberOptional = memberRepository.findById(memberId);
		if (memberOptional.isPresent()) {
			ClubMember member = memberOptional.get();
			return new MemberDto(member);
		}
		return null;
	}

// Get a specific member with their outings
	@GetMapping("/members/{memberId}/outings")
	@Transactional
	public List<Outing> getMemberOutings(@PathVariable Long memberId) {
		Optional<ClubMember> memberOptional = memberRepository.findById(memberId);
		if (memberOptional.isPresent()) {
			ClubMember member = memberOptional.get();
			return outingRepository.findByCreatedBy(member);
		}
		return null;
	}

	@PutMapping("/members/{memberId}")
	public ClubMember modifyMember(@PathVariable Long memberId, @RequestBody ClubMember memberDetails) {
		Optional<ClubMember> memberOptional = memberRepository.findById(memberId);
		if (memberOptional.isPresent()) {
			ClubMember member = memberOptional.get();
			// Update member details based on memberDetails
			member.setName(memberDetails.getName());
			member.setSurname(memberDetails.getSurname());
			member.setEmailId(memberDetails.getEmailId());
			return memberRepository.save(member);
		}
		return null;
	}

// Method to extract member details from JWT token
	private ClubMember extractMemberFromToken(String jwtToken) {
		try {

			@SuppressWarnings("deprecation")
			Claims claims = Jwts.parser().setSigningKey(
					"cd8b9a95db1f6e390ff0c31a95bc6ebf5274fc21c5e8acce403474a5f23b9510e2f4ad763cf9d49481e3f514c296d35a6b503bd6e6ffb3242b64ad9a5c6c9a25") // generation
					.parseClaimsJws(jwtToken).getBody();

			String emailId = claims.getSubject();

			ClubMember loggedInMember = memberRepository.findByEmailId(emailId);
			return loggedInMember;
		} catch (Exception e) {

			return null;
		}
	}

}