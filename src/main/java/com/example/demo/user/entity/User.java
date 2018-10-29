package com.example.demo.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	private String documentId;

	private String firstName;
	private String lastName;
	private String email;
	private String password;
}
