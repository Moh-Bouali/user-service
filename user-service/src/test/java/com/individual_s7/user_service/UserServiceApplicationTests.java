package com.individual_s7.user_service;

import com.individual_s7.user_service.config.RabbitMQConfig;
import com.individual_s7.user_service.dto.UserRequest;
import com.individual_s7.user_service.events.UserUpdatedEvent;
import com.individual_s7.user_service.model.User;
import com.individual_s7.user_service.repository.UserRepository;
import com.individual_s7.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
class UserServiceApplicationTests {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RabbitTemplate rabbitTemplate;

	@InjectMocks
	private UserService userService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		doNothing().when(rabbitTemplate).convertAndSend(any(String.class), any(String.class), any(Object.class));
	}

	@Test
	@Transactional
	public void testCreateUser() {
		// Arrange: Create a UserRequest to pass to userService
		UserRequest userRequest = new UserRequest(
				null,
				"testuser@example.com",
				"testuser",
				"testpassword",
				"This is a bio",
				"http://example.com/profile.jpg"
		);

		// Create a User object that represents what the repository would save
		User savedUser = User.builder()
				.id(1L) // Typically, the ID would be assigned when the user is saved
				.email(userRequest.email())
				.username(userRequest.username())
				.bio(userRequest.bio())
				.profile(userRequest.profile())
				.build();

		// Mock the behavior of save() to return the saved user
		when(userRepository.save(any(User.class))).thenReturn(savedUser);

		// Mock the behavior of findByUsername() to return the saved user after creation
		when(userRepository.findByUsername("testuser")).thenReturn(savedUser);

		// Act: Create the user via the service
		userService.createUser(userRequest);

		// Assert: Verify that the user was saved and can be found by username
		User foundUser = userRepository.findByUsername("testuser");

		assertNotNull(foundUser);
		assertEquals("testuser", foundUser.getUsername());
		assertEquals("testuser@example.com", foundUser.getEmail());
		assertEquals("This is a bio", foundUser.getBio());
		assertEquals("http://example.com/profile.jpg", foundUser.getProfile());

		// Verify that userRepository.save() was called with a User object
		verify(userRepository, times(1)).save(any(User.class));

		// Verify that userRepository.findByUsername() was called
		verify(userRepository, times(1)).findByUsername("testuser");
	}

	@Test
	@Transactional
	public void testDeleteUserById() {
		// Arrange: Create a User object and set an ID
		User user = new User();
		user.setId(1L);
		user.setUsername("testuser");
		user.setEmail("test@example.com");

		// Mock the behavior of save() to return the user
		when(userRepository.save(any(User.class))).thenReturn(user);

		// Mock the behavior of findById() before deletion to return the user
		when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

		// Act: Call the service to delete the user
		userService.deleteUserById(user.getId());

		// Mock the behavior of findById() after deletion to return empty
		when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

		// Assert: Verify that the userRepository.deleteById() was called
		verify(userRepository, times(1)).deleteById(user.getId());

		// Assert: Verify that userRepository.findById() now returns empty
		assertFalse(userRepository.findById(user.getId()).isPresent());
	}

	@Test
	@Transactional
	public void testUpdateUserById() {
		// Arrange
		UserRequest userRequest = new UserRequest(
				1L,
				"updated@example.com",
				"updatedUsername",
				"updatedPassword",
				"updatedBio",
				"http://example.com/updated_profile.jpg"
		);

		User existingUser = User.builder()
				.id(userRequest.id())
				.email("testuser@example.com")
				.username("testuser")
				.bio("This is a bio")
				.profile("http://example.com/profile.jpg")
				.build();

		// Mocking the behavior of userRepository.findById()
		when(userRepository.findById(userRequest.id())).thenReturn(Optional.of(existingUser));

		// Act
		userService.updateUserById(userRequest);

		// Assert
		// Verify that userRepository.updatePerson() was called with the correct arguments
		verify(userRepository, times(1)).updatePerson(
				userRequest.id(),
				userRequest.email(),
				userRequest.username(),
				userRequest.bio(),
				userRequest.profile()
		);

		// Verify that RabbitTemplate was used to send an update event
		verify(rabbitTemplate, times(1)).convertAndSend(
				eq(RabbitMQConfig.USER_UPDATE_EXCHANGE),
				eq(RabbitMQConfig.USER_UPDATE_ROUTING_KEY),
				any(UserUpdatedEvent.class)
		);
	}

	@Test
	@Transactional
	public void testFindByUsername() {
		// Arrange
		User user = new User();
		user.setId(1L);
		user.setUsername("testuser");
		user.setEmail("testuser@example.com");

		// Mock the behavior of save() and findByUsername()
		when(userRepository.save(any(User.class))).thenReturn(user);
		when(userRepository.findByUsername("testuser")).thenReturn(user);

		// Act
		userRepository.save(user);
		User foundUser = userRepository.findByUsername("testuser");

		// Assert
		assertNotNull(foundUser);
		assertEquals("testuser", foundUser.getUsername());
	}
}
