package com.company.automaticseller.server.user.repository;

import com.company.automaticseller.server.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
	User findUserByDocumentId(String documentId);

	User findUserByEmailAndPassword(String email, String password);
}
