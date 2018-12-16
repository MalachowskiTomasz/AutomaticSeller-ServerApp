package com.example.demo.user.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Users")
@Data
public class User {

	@Id
	@Column(name = "Document_ID")
	private String documentId;

	@Column(name = "Validated")
	private boolean validated;

	@Column(name = "First_Name")
	private String firstName;

	@Column(name = "Last_Name")
	private String lastName;

	@Column(name = "Email")
	private String email;

	@Column(name = "Password")
	private String password;

	@Column(name = "Entrance_ID")
	private Integer entranceId;

	private User() {

	}

	public User(String documentId, String firstName, String lastName, String email, String password) {
		this.documentId = documentId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public void generateEntranceID() {
		if (entranceId == null)
			entranceId = Objects.hash(documentId, firstName, lastName, email, password);
	}
}
