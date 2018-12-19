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

	/**
	 * Adds user to database
	 *
	 * @param documentId User's serial number of identity card
	 * @param email      User e-mail
	 * @param password   User password
	 * @param firstName  First name
	 * @param lastName   Last name
	 * @return If user doesn't already exist in database information "User has been added". Otherwise "User already
	 * exists in database"
	 */
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
				ResponseEntity.ok("User has been added") :
				ResponseEntity.status(DUPLICATED_USER_STATUS)
						.body("User already exists in database");
	}

	/**
	 * Displays all users in database
	 *
	 * @return List of users in database in JSON format
	 */
	@RequestMapping(value = "/getAll", method = GET)
	public List<User> getAllUsers() {
		return userService.getAllUsers();
	}

	/**
	 * Authenticates users
	 *
	 * @param email    User e-mail
	 * @param password User password
	 * @return If user exists in database and e-mail and password are correct - all information about the user in
	 * JSON format. Otherwise information "User doesn't exist, login or password is invalid or user is not validated"
	 */
	@RequestMapping(value = "/authenticate", method = GET)
	public ResponseEntity<Object> authenticateUser(@RequestParam String email,
												   @RequestParam String password) {
		Optional<User> u = userService.authenticate(email, password);
		return u.isPresent() ?
				ResponseEntity.ok(u.get()) :
				ResponseEntity.status(NOT_FOUND_STATUS)
						.body("User doesn't exist, login or password is invalid or user is not validated");
	}

	/**
	 * Makes user account validated
	 *
	 * @param documentId User's serial number of identity document
	 * @return If user exists in database information "User has been validated". Otherwise "User not found"
	 */
	@RequestMapping(value = "/validate", method = GET)
	public ResponseEntity validateUser(@RequestParam String documentId) {
		return userService.validate(documentId) ?
				ResponseEntity.ok("User has been validated") :
				ResponseEntity.status(NOT_FOUND_STATUS).body("User not found");
	}
}
