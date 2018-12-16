package com.example.demo.user.services;

import com.example.demo.user.entity.User;
import com.example.demo.user.repository.UserRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	private final UserRepository userRepository;

	@Autowired
	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public boolean addUser(User user) {
		if (userRepository.findUserByDocumentId(user.getDocumentId()) != null)
			return false;

		user.setValidated(true);
		userRepository.save(user);
		return true;
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public Optional<User> authenticate(String email, String password) {
		val user = findUser(email, password);
		if (!user.isPresent() || !user.get().isValidated())
			return Optional.empty();

		User u = user.get();
		u.generateEntranceID();
		userRepository.save(u);
		return user;
	}

	public boolean validate(String documentId) {
		val u = findUser(documentId);
		if (!u.isPresent())
			return false;

		u.get().setValidated(true);
		return true;
	}

	private Optional<User> findUser(String documentId) {
		return Optional.ofNullable(userRepository.findUserByDocumentId(documentId));
	}

	private Optional<User> findUser(String email, String password) {
		return Optional.ofNullable(userRepository.findUserByEmailAndPassword(email, password));
	}
}
