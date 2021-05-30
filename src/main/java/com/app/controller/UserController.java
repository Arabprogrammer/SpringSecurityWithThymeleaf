package com.app.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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


	@GetMapping({ "/", "/signIn" })
	public String login() {
		return "login";
	}

	@GetMapping("/signUp")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}

	@GetMapping("/home")
	public String home() {
		return "home";
	}

//	required
//	minlength="8"
	/*
	 * The BindingResult must come right after the model object that is validated or
	 * else Spring will fail to validate the object and throw an exception.
	 */
	@PostMapping(path = "/registration")
	public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		List<String> errors = checkIfPasswordAndConfirmPasswordValid(user);
		if (!errors.isEmpty()) {
			model.addAttribute("errors", errors);
			return "registration";
		}
		Optional<User> userExists = userRepo.findByEmail(user.getEmail());
		if (userExists.isPresent()) {
			errors.add(user.getEmail() + " is already being used");
			model.addAttribute("errors", errors);
			return "registration";
		} else {
			String firstName = user.getFirstName();
			String lastName = user.getLastName();
			String email = user.getEmail();
			String password = user.getPassword();
			LocalDate birthday = user.getBirthday();
			Set<Role> roles = roleRepo.findByRole("USER").stream().collect(Collectors.toSet());
			User toSave = new User(firstName, lastName, birthday, email, new BCryptPasswordEncoder().encode(password),
					false, true, roles);
			userRepo.save(toSave);
			return "login";
		}
	}

	private List<String> checkIfPasswordAndConfirmPasswordValid(User user) {
		List<String> errors = new ArrayList<>();
		String password = user.getPassword();
		String repeatPassword = user.getConfirmPassword();
		if (!Objects.equals(repeatPassword, password)) {
			errors.add("Passwords Don't Match.");
		}
		return errors;
	}

}
