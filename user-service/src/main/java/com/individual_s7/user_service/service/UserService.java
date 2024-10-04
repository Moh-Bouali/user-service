package com.individual_s7.user_service.service;

import com.individual_s7.user_service.dto.UserRequest;
import com.individual_s7.user_service.model.User;
import com.individual_s7.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void createUser(UserRequest userRequest){
        User user = User.builder()
                .email(userRequest.email())
                .username(userRequest.username())
                .bio(userRequest.bio())
                .profile(userRequest.profile())
                .build();
        userRepository.save(user);
    }
    public void updateUserById(UserRequest userRequest){
        User newUser = User.builder()
                .id(userRequest.id())
                .email(userRequest.email())
                .username(userRequest.username())
                .bio(userRequest.bio())
                .profile(userRequest.profile())
                .build();
        userRepository.updatePerson(newUser.getId(),newUser.getEmail(), newUser.getUsername(), newUser.getBio(), newUser.getProfile());
    }

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    public List<User> findUserByUsernameOrEmail(String query){
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }

    public boolean userExists(String username, String email){
        Optional<User> user = userRepository.findByUsernameOrEmail(username, email);
        return user.isPresent();
    }
}
