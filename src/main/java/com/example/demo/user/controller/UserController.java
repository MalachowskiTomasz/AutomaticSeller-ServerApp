package com.example.demo.user.controller;

import com.example.demo.user.entity.User;
import com.example.demo.user.services.UserService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final int NOT_FOUND_STATUS = 420;
	private static final int DUPLICATED_USER_STATUS = 293;

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}

	@RequestMapping(value = "/add", method = POST)
	public ResponseEntity<String> addUser(@RequestParam String documentId,
										  @RequestParam String email,
										  @RequestParam String password,
										  @RequestParam(required = false) String firstName,
										  @RequestParam(required = false) String lastName) {
		val u = new User.UserBuilder(documentId, email, password)
				.withFirstName(firstName)
				.withLastName(lastName)
				.build();
		return userService.addUser(u) ?
				ResponseEntity.ok().build() :
				ResponseEntity.status(DUPLICATED_USER_STATUS)
						.body("User already exist in database");
	}

	@RequestMapping(value = "/getAll", method = GET)
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	@RequestMapping(value = "/authenticate", method = GET)
	public ResponseEntity<Object> authenticateUser(@RequestParam String email,
												   @RequestParam String password) {
		Optional<User> u = userService.authenticate(email, password);
		return u.isPresent() ?
				ResponseEntity.ok(u.get()) :
				ResponseEntity.status(NOT_FOUND_STATUS)
						.body("User doesn't exist, login or password is invalid or user is not validated");
	}

	@RequestMapping(value = "/validate", method = GET)
	public ResponseEntity validateUser(@RequestParam String documentId) {
		return userService.validate(documentId) ?
				ResponseEntity.ok("User has been validated") :
				ResponseEntity.status(NOT_FOUND_STATUS).body("User not found");
	}
}
