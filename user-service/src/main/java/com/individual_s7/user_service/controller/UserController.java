package com.individual_s7.user_service.controller;

import com.individual_s7.user_service.dto.UserRequest;
import com.individual_s7.user_service.dto.UserResponse;
import com.individual_s7.user_service.mapper.UserMapper;
import com.individual_s7.user_service.model.User;
import com.individual_s7.user_service.service.KeycloakAdminService;
import com.individual_s7.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final KeycloakAdminService keycloakAdminService;
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {
        if(userService.userExists(userRequest.username(), userRequest.email())) {
            return ResponseEntity.badRequest().body("Username or password already exists");
        }
        else{
            // Save the user to your local database
            userService.createUser(userRequest);
            // Register the user in Keycloak
            keycloakAdminService.registerUserInKeycloak(userRequest.username(), userRequest.password());
            return ResponseEntity.ok("User created and registered in Keycloak");
        }
    }


    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        // Fetch the current user details from the database
        User currentUser = userService.findUserById(userRequest.id());
        userService.updateUserById(userRequest);
        // Update the user in Keycloak
        keycloakAdminService.updateUserInKeycloak(currentUser.getUsername(), userRequest.username(), userRequest.email());
        // Fetch the updated user from the database
        User updatedUser = userService.findUserById(userRequest.id());
        UserResponse userResponse = UserMapper.toUserResponse(updatedUser);
        // Return the updated user details in the response
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfileJWT(Authentication authentication) {
        // Extract JWT from the Authentication object
        Jwt jwt = (Jwt) authentication.getPrincipal();
        // Extract the username from the JWT claims (typically 'preferred_username' or 'sub')
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) {
            username = jwt.getSubject(); // Fallback to 'sub' claim if 'preferred_username' is not present
        }
        User user = userService.findUserByUsername(username);
        UserResponse userResponse = UserMapper.toUserResponse(user);
        // Now, you can use the 'username' to return the user profile
        return ResponseEntity.ok(userResponse);
    }

    //return user by search query from frontend
    @GetMapping("/search")
    public List<UserResponse> searchUser(@RequestParam("query") String query) {
        List<User> users = userService.findUserByUsernameOrEmail(query);
        return users.stream().map(UserMapper::toUserResponse).collect(Collectors.toList());
    }
}

