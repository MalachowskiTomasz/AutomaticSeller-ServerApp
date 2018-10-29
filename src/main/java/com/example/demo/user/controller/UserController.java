package com.example.demo.user.controller;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/user")
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@RequestMapping(value = "/findAll", method = GET)
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	@RequestMapping(value = "/authenticate", method = GET)
	public ResponseEntity authenticateUser(@RequestParam String email,
										   @RequestParam String password) {
		return userRepository.findUserByEmailAndPassword(email, password) != null ?
				ResponseEntity.ok("User has been authenticated") :
				ResponseEntity.status(420).body("User hasn't been found");
	}
}
