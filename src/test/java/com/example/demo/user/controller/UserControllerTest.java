package com.example.demo.user.controller;

import com.example.demo.user.entity.User;
import com.example.demo.user.services.UserService;
import lombok.val;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

	@Mock
	private UserService userService;

	private UserController userController;

	private String userDocumentId = "123";
	private String userEmail = "hello@gmail.com";
	private String userPassword = "hello";
	private String userFirstName = "Tomek";
	private String userLastName = "M";

	@Before
	public void setUp() {
		userController = new UserController(userService);
	}

	@Test
	public void shouldAddUserToDatabaseIfNotFoundInDatabase() {
		when(userService.addUser(any(User.class))).thenReturn(true);

		val result = userController.addUser(userDocumentId, userEmail, userPassword, userFirstName, userLastName);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("User has been added");
	}

	@Test
	public void shouldNotAddUserToDatabaseIfAlreadyExists() {
		when(userService.addUser(any(User.class))).thenReturn(false);

		val result = userController.addUser(userDocumentId, userEmail, userPassword, userFirstName, userLastName);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("User already exists in database");
	}

	@Test
	public void shouldCorrectlyRetrieveAllUsersFromDatabase() {
		val userList = Lists.newArrayList(
				new User.UserBuilder(userDocumentId, userEmail, userPassword).build(),
				new User.UserBuilder("124", "he@test.com", "hello").build()
		);
		when(userService.getAllUsers()).thenReturn(userList);

		val result = userController.getAllUsers();

		assertThat(result).hasSize(2);
		assertThat(result).containsAll(userList);
	}

	@Test
	public void shouldAuthenticateUserIfExistsInDatabase() {
		val u = new User.UserBuilder(userDocumentId, userEmail, userPassword).build();
		when(userService.authenticate(userEmail, userPassword)).thenReturn(Optional.of(u));

		val result = userController.authenticateUser(userEmail, userPassword);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo(u);
	}

	@Test
	public void shouldNotAuthenticateUserIfDoesNotExistsInDatabase() {
		when(userService.authenticate(userEmail, userPassword)).thenReturn(Optional.empty());

		val result = userController.authenticateUser(userEmail, userPassword);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("User doesn't exist, login or password is invalid or user is not validated");
	}

	@Test
	public void shouldValidateUserIfExistInDatabase() {
		when(userService.validate(userDocumentId)).thenReturn(true);

		val result = userController.validateUser(userDocumentId);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("User has been validated");
	}

	@Test
	public void shouldNotValidateUserIfDoesNotExistInDatabase() {
		when(userService.validate(userDocumentId)).thenReturn(false);

		val result = userController.validateUser(userDocumentId);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).isEqualTo("User has not been found");
	}
}