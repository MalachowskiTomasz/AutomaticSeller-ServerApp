package com.company.automaticseller.server.user.services;

import com.company.automaticseller.server.user.entity.User;
import com.company.automaticseller.server.user.repository.UserRepository;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	private UserService userService;
	private String userDocumentId = "123";
	private String userEmail = "hello@gmail.com";
	private String userPassword = "hello";

	@Before
	public void setUp() {
		userService = new UserService(userRepository);
	}

	@Test
	public void shouldAddUserIfNotFoundAnyInDatabase() {
		val captor = ArgumentCaptor.forClass(User.class);
		val u = new User.UserBuilder(userDocumentId, userEmail, userPassword).build();
		when(userRepository.findUserByDocumentId(anyString())).thenReturn(null);
		doReturn(null).when(userRepository).save(captor.capture());

		val result = userService.addUser(u);

		assertThat(result).isTrue();
		assertThat(captor.getValue()).isEqualTo(u);
	}

	@Test
	public void shouldNotAddUser_ifUserWithSameDocumentIdExistsInDatabase() {
		val databaseUser = new User.UserBuilder(userDocumentId, "user@company.com", "password").build();
		when(userRepository.findUserByDocumentId(anyString())).thenReturn(databaseUser);

		val u = new User.UserBuilder(userDocumentId, userEmail, userPassword).build();
		val result = userService.addUser(u);

		assertThat(result).isFalse();
	}

	@Test
	public void shouldRetrieveAllUsersFromDatabase() {
		val users = Lists.newArrayList(
				new User.UserBuilder(userDocumentId, userEmail, userPassword).build(),
				new User.UserBuilder("124", "test@gmail.com", "dome").build()
		);
		when(userRepository.findAll()).thenReturn(users);

		val result = userService.getAllUsers();

		assertThat(result).containsAll(users);
	}

	@Test
	public void shouldAuthenticateUser_whenExistsInDatabaseIsValidatedAndHasEntranceId() {
		val u = new User.UserBuilder(userDocumentId, userEmail, userPassword)
				.isValidated(true)
				.withEntranceId(1234).build();
		when(userRepository.findUserByEmailAndPassword(userEmail, userPassword))
				.thenReturn(u);
		doReturn(null).when(userRepository).save(any(User.class));

		val result = userService.authenticate(userEmail, userPassword);

		assertThat(result).isPresent();
		val user = result.get();
		assertThat(user).isEqualTo(u);
		assertThat(user.getEntranceId()).isEqualTo(1234);
	}

	@Test
	public void shouldAuthenticateUserAndGenerateEntranceId_whenExistsInDatabaseIsValidatedAndHasNoEntranceId() {
		val captor = ArgumentCaptor.forClass(User.class);
		val u = new User.UserBuilder(userDocumentId, userEmail, userPassword)
				.isValidated(true)
				.withEntranceId(null).build();
		when(userRepository.findUserByEmailAndPassword(userEmail, userPassword))
				.thenReturn(u);
		doReturn(null).when(userRepository).save(captor.capture());

		val result = userService.authenticate(userEmail, userPassword);

		assertThat(result).isPresent();
		val user = result.get();
		assertThat(user).isEqualTo(u);
		assertThat(user.getEntranceId()).isNotNull();
	}


	@Test
	public void shouldNotAuthenticateUser_ifIsNotValidated() {
		val u = new User.UserBuilder(userDocumentId, userEmail, userPassword)
				.isValidated(false).build();
		when(userRepository.findUserByEmailAndPassword(userEmail, userPassword))
				.thenReturn(u);

		val result = userService.authenticate(userEmail, userPassword);

		assertThat(result).isNotPresent();
	}

	@Test
	public void shouldNotAuthenticateUser_ifDoesntExistInDatabase() {
		when(userRepository.findUserByEmailAndPassword(userEmail, userPassword))
				.thenReturn(null);

		val result = userService.authenticate(userEmail, userPassword);

		assertThat(result).isNotPresent();
	}

	@Test
	public void shouldValidateUser_ifExistsInDatabase() {
		val captor = ArgumentCaptor.forClass(User.class);
		val u = new User.UserBuilder(userDocumentId, userEmail, userPassword)
				.isValidated(false).build();
		when(userRepository.findUserByDocumentId(userDocumentId)).thenReturn(u);
		doReturn(null).when(userRepository).save(captor.capture());

		val result = userService.validate(userDocumentId);

		assertThat(result).isTrue();
		val user = captor.getValue();
		assertThat(user).isEqualTo(u);
		assertThat(user.isValidated()).isTrue();
	}

	@Test
	public void shouldNotValidateUser_ifDoesNotExistInDatabase() {
		when(userRepository.findUserByDocumentId(anyString())).thenReturn(null);

		val result = userService.validate(userDocumentId);

		assertThat(result).isFalse();
	}
}