package com.example.demo.user.repository;

import com.example.demo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
	User findUserByDocumentId(String documentId);

	User findUserByEmailAndPassword(String email, String password);
}
