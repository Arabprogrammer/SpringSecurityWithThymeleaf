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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;


@Entity
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "firstname")
	private String firstName;

	@Column(name = "lasttname")
	private String lastName;

	@Column(name = "birthday", columnDefinition = "DATE")
	private LocalDate birthday;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "password")
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

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", birthday=" + birthday
				+ ", email=" + email + ", password=" + password + ", isLocked=" + isLocked + ", isEnabled=" + isEnabled
				+ ", roles=" + roles + "]";
	}
}
