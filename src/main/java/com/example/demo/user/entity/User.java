package com.example.demo.user.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@Data
public class User {

	@Id
	private String documentId;

	private boolean validated;

	private String firstName;
	private String lastName;
	private String email;
	private String password;

	private Integer entranceId;

	private User(){

	}

	public User(String documentId, String firstName, String lastName, String email, String password) {
		this.documentId = documentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public void generateEntranceID() {
		if(entranceId == null)
			entranceId = Objects.hash(documentId, firstName, lastName, email, password);
	}
}
