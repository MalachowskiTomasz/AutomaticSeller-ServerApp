package com.example.demo.user.controller;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/user")
public class UserController {

	private static final int NOT_FOUND_STATUS = 420;
	private static final int DUPLICATED_USER_STATUS = 293;
	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/add", method = POST)
	public ResponseEntity<String> addUser(@RequestParam String documentId,
			@RequestParam String email,
								  @RequestParam String password,
								  @RequestParam String firstName,
								  @RequestParam String lastName) {
		User u = new User(documentId, firstName, lastName, email, password);

		if (!userRepository.findById(documentId).isPresent()) {
			userRepository.save(u);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.status(DUPLICATED_USER_STATUS).body("User already exist in database");
	}

	@RequestMapping(value = "/findAll", method = GET)
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@RequestMapping(value = "/authenticate", method = GET)
	public ResponseEntity<String> authenticateUser(@RequestParam String email,
										   @RequestParam String password) {
		User user = userRepository.findUserByEmailAndPassword(email, password);
		if (user != null)
			return ResponseEntity.ok(String.valueOf(user.getEntranceID()));
		else
			return ResponseEntity.status(NOT_FOUND_STATUS).body("User doesn't exist or Invalid login or password");
	}

	@RequestMapping(value = "/validate", method = GET)
	public ResponseEntity validateUser(@RequestParam String documentId) {
		val user = userRepository.findUserByDocumentId(documentId);
		if (user == null) return ResponseEntity.status(420).body("User not found");
		user.setValidated(true);
		return ResponseEntity.ok("User has been validated");
	}
}
