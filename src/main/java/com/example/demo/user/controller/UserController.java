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

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/add", method = PUT)
	public ResponseEntity addUser(@RequestParam String documentId,
								  @RequestParam String firstName,
								  @RequestParam String lastName,
								  @RequestParam String email,
								  @RequestParam String password) {
		val user = new User(documentId, firstName, lastName, email, password);
		userRepository.save(user);
		return ResponseEntity.ok("User has been created");
	}

	@RequestMapping(value = "/delete", method = DELETE)
	public ResponseEntity deleteUser(@RequestParam String documentId) {
		val user = userRepository.findUserByDocumentId(documentId);
		if (user == null)
			return ResponseEntity.status(420).body("User has not been found");
		userRepository.delete(user);
		return ResponseEntity.ok("User has been deleted");
	}

	@RequestMapping(value = "/findAll", method = GET)
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@RequestMapping(value = "/authenticate", method = GET)
	public ResponseEntity authenticateUser(@RequestParam String email,
										   @RequestParam String password) {
		User user = userRepository.findUserByEmailAndPassword(email, password);
		if (user != null)
			return ResponseEntity.ok(user.getEntranceID());
		else
			return ResponseEntity.status(420).body("User doesn't exist or Invalid login or password");
	}

	@RequestMapping(value = "/validate", method = GET)
	public ResponseEntity validateUser(@RequestParam String documentId) {
		val user = userRepository.findUserByDocumentId(documentId);
		if (user == null) return ResponseEntity.status(420).body("User not found");
		user.setValidated(true);
		return ResponseEntity.ok("User has been validated");
	}
}
