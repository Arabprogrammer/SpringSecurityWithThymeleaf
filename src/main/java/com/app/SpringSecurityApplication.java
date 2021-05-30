package com.app;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.model.Role;
import com.app.model.User;
import com.app.repo.RoleRepo;
import com.app.repo.UserRepo;

@SpringBootApplication
public class SpringSecurityApplication {

	@Autowired
	UserRepo userRepo;
	@Autowired
	RoleRepo roleRepo;
	@Autowired
	PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void addFirtUser() {
		String email = "Arabprogrammer@gmail.com";
		Optional<User> user = userRepo.findByEmail(email);
		Optional<Role> adminrRole = roleRepo.findByRole("ADMIN");
		Optional<Role> userRole = roleRepo.findByRole("USER");
		if (!user.isPresent()) {
			if (adminrRole.isPresent() && userRole.isPresent()) {
				Set<Role> roles = Set.of(adminrRole.get(), userRole.get());
				User addedUser = new User("Arabprogrammer", "Arabprogrammer", LocalDate.of(1990, 1, 1),
						"Arabprogrammer@gmail.com", passwordEncoder.encode("12345678"), false, true, roles);
				userRepo.save(addedUser);
			} else {
				Role savedAdminRole = roleRepo.save(new Role("ADMIN"));
				Role savedUserRole = roleRepo.save(new Role("USER"));
				Set<Role> roles = Set.of(savedAdminRole, savedUserRole);
				User addedUser = new User("Arabprogrammer", "Arabprogrammer", LocalDate.of(1990, 1, 1),
						"Arabprogrammer@gmail.com", passwordEncoder.encode("12345678"), false, true, roles);
				userRepo.save(addedUser);
			}

		} else {
			return;
		}
	}

}
