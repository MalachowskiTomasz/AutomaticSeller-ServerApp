package com.company.automaticseller.server.user.entity;

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

	private User(String documentId, String firstName, String lastName, String email, String password,
				 boolean validated, Integer entranceId) {
		this.documentId = documentId;
		this.validated = validated;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.entranceId = entranceId;
	}

	public void generateEntranceID() {
		if (entranceId == null)
			entranceId = Objects.hash(documentId, firstName, lastName, email, password);
	}

	public static class UserBuilder {
		private String documentId;
		private boolean validated;
		private String firstName;
		private String lastName;
		private String email;
		private String password;
		private Integer entranceId;

		public UserBuilder(String documentId, String email, String password) {
			this.documentId = documentId;
			this.email = email;
			this.password = password;
		}

		public UserBuilder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}

		public UserBuilder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public UserBuilder withEntranceId(Integer entranceId) {
			this.entranceId = entranceId;
			return this;
		}

		public UserBuilder isValidated(boolean validated) {
			this.validated = validated;
			return this;
		}

		public User build() {
			return new User(documentId, firstName, lastName, email, password, validated, entranceId);
		}
	}
}
