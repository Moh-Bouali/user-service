//package com.individual_s7.user_service;
//
//import com.individual_s7.user_service.dto.UserRequest;
//import com.individual_s7.user_service.model.User;
//import com.individual_s7.user_service.repository.UserRepository;
//import com.individual_s7.user_service.service.UserService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Import(TestcontainersConfiguration.class)
//@SpringBootTest
//class UserServiceApplicationTests {
//
//	@Autowired
//	private UserService userService;
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Test
//	@Transactional
//	//@Rollback(false)
//	public void testCreateUser() {
//		// Arrange
//		UserRequest userRequest = new UserRequest(
//				null,
//				"testuser@example.com",
//				"testuser",
//				"testpassword",
//				"This is a bio",
//				"http://example.com/profile.jpg"
//		);
//
//		// Act
//		userService.createUser(userRequest);
//
//		// Assert
//		User foundUser = userRepository.findByUsername("testuser");
//		assertNotNull(foundUser);
//		assertEquals("testuser", foundUser.getUsername());
//		assertEquals("testuser@example.com", foundUser.getEmail());
//		assertEquals("This is a bio", foundUser.getBio());
//		assertEquals("http://example.com/profile.jpg", foundUser.getProfile());
//	}
//
//	@Test
//	@Transactional
//	public void testDeleteUserById() {
//		// Arrange
//		User user = new User();
//		user.setUsername("testuser");
//		user.setEmail("test@example.com");
//		userRepository.save(user);
//
//		// Act
//		userService.deleteUserById(user.getId());
//
//		// Assert
//		assertFalse(userRepository.findById(user.getId()).isPresent());
//	}
//
//	@Test
//	@Transactional
//	public void testUpdateUserById() {
//		// Arrange
//		UserRequest userRequest = new UserRequest(
//				null,
//				"testuser@example.com",
//				"testuser",
//				"testpassword",
//				"This is a bio",
//				"http://example.com/profile.jpg"
//		);
//
//		// Act
//		userService.createUser(userRequest);
//
//		// Assert
//		User foundUser = userRepository.findByUsername("testuser");
//
//		foundUser.setEmail("test@example.com");
//		assertEquals("test@example.com", foundUser.getEmail());
//	}
//
//	@Test
//	@Transactional
//	public void testFindByUsername() {
//		// Arrange
//		User user = new User();
//		user.setUsername("testuser");
//		user.setEmail("testuser@example.com");
//		userRepository.save(user);
//		userRepository.flush();
//
//		// Act
//		User foundUser = userRepository.findByUsername("testuser");
//
//		// Assert
//		assertNotNull(foundUser);
//		assertEquals("testuser", foundUser.getUsername());
//	}
//}