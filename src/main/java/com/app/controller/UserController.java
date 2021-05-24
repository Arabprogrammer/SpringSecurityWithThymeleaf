package com.app.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.app.model.Role;
import com.app.model.User;
import com.app.repo.RoleRepo;
import com.app.repo.UserRepo;

@Controller
public class UserController {

	@Autowired
	UserRepo userRepo;
	@Autowired
	RoleRepo roleRepo;

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^(.+)@(.+)$", Pattern.CASE_INSENSITIVE);

	@GetMapping({ "/", "/signIn" })
	public String login() {
		return "login";
	}

	@GetMapping("/signUp")
	public String signUp() {
		return "registration";
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}

	@PostMapping(path = "/registration")
	public String createNewUser(@RequestParam Map<String, String> requestBody, Model model) {
		List<String> errors = getErrors(requestBody);
		if (!errors.isEmpty()) {
			model.addAttribute("errors", errors);
			return "registration";
		}
		Optional<User> userExists = userRepo.findByEmail(requestBody.get("email"));
		if (userExists.isPresent()) {
			errors.add(requestBody.get("email") + " is already being used");
			model.addAttribute("errors", errors);
			return "registration";
		} else {
			String firstName = requestBody.get("firstName");
			String lastName = requestBody.get("lastName");
			String email = requestBody.get("email");
			String password = requestBody.get("password");
			String birthday = requestBody.get("birthday");
			Set<Role> roles = roleRepo.findByRole("USER").stream().collect(Collectors.toSet());
			User user = new User(firstName, lastName, LocalDate.parse(birthday), email,
					new BCryptPasswordEncoder().encode(password), false, true, roles);
			userRepo.save(user);
			return "login";
		}
	}

	private List<String> getErrors(Map<String, String> requestBody) {
		List<String> errors = new ArrayList<>();
		String firstName = requestBody.get("firstName");
		if (StringUtils.trimToEmpty(firstName).isEmpty() || firstName.length() < 2) {
			errors.add("First name is not valid.");
		}
		String lastName = requestBody.get("lastName");
		if (StringUtils.trimToEmpty(lastName).isEmpty() || lastName.length() < 2) {
			errors.add("Last name is not valid.");
		}
		String email = requestBody.get("email");
		if (StringUtils.trimToEmpty(email).isEmpty() || !isEmailValid(email)) {
			errors.add("Email is not valid.");
		}
		String password = requestBody.get("password");
		if (StringUtils.trimToEmpty(password).isEmpty()) {
			errors.add("Password is not valid.");
		}
		String repeatPassword = requestBody.get("repeatPassword");
		if (StringUtils.trimToEmpty(repeatPassword).isEmpty()) {
			errors.add("Confirm password is not valid.");
		}
		if (!Objects.equals(repeatPassword, password)) {
			errors.add("Passwords Don't Match.");
		}
		String birthday = requestBody.get("birthday");
		if (StringUtils.trimToEmpty(birthday).isEmpty()) {
			errors.add("Birthday is not valid.");
		}
		return errors;
	}

	public static boolean isEmailValid(String email) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
		return matcher.matches();
	}

}
