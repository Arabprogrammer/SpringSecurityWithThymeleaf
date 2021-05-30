package com.app.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "firstname")
	@NotNull(message = "FirstName cannot be null")
	@Size(min = 2, max = 20, message = "Firstname must be between 2 and 20 characters")
	private String firstName;

	@Column(name = "lastname")
	@NotNull(message = "LastName cannot be null")
	@Size(min = 2, max = 20, message = "LastName must be between 2 and 20 characters")
	private String lastName;

	@Column(name = "birthday", columnDefinition = "DATE")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull(message = "Birthday cannot be null")
	@Past(message = "Birthday must be a past date")
	private LocalDate birthday;

	@Column(name = "email", unique = true)
	@NotEmpty(message = "Email cannot be null")
	@Email(message = "Email should be valid")
	private String email;

	@Transient
	@NotNull(message = "Confirm Password cannot be null")
	@Size(min = 8,max = 20, message = "Confirm Password must be between 8 and 16 characters")
	private String confirmPassword;

	@Column(name = "password")
	@NotNull(message = "Password cannot be null")
	@Size(min = 8,max = 20, message = "Password must be between 8 and 16 characters")
	private String password;

	@Column(name = "is_locked")
	private Boolean isLocked;

	@Column(name = "is_enabled")
	private Boolean isEnabled;

	@ManyToMany(fetch = FetchType.EAGER)
	@Fetch(FetchMode.JOIN)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	public Set<Role> getrRoles() {
		return this.roles;
	}

	public User() {

	}

	public User(String firstName, String last_name, LocalDate birthday, String email, String password, Boolean isLocked,
			Boolean isEnabled, Set<Role> roles) {
		this.firstName = firstName;
		this.lastName = last_name;
		this.birthday = birthday;
		this.email = email;
		this.password = password;
		this.isLocked = isLocked;
		this.isEnabled = isEnabled;
		this.roles = roles;
	}

	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public boolean isAccountLocked() {
		return isLocked;
	}

	public boolean isEnabled() {
		return isEnabled;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthday=" + birthday
				+ ", email=" + email + ", confirmPassword=" + confirmPassword + ", password=" + password + ", isLocked="
				+ isLocked + ", isEnabled=" + isEnabled + ", roles=" + roles + "]";
	}
}
