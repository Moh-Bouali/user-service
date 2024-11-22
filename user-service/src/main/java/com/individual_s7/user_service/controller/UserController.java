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
            keycloakAdminService.registerUserInKeycloak(userRequest.username(), userRequest.password());
            // Save the user to your local database
            if(keycloakAdminService.registeredUserInKeycloak){
                userService.createUser(userRequest);
                return ResponseEntity.ok("User created and registered in Keycloak");
            }
            // Register the user in Keycloak
            return ResponseEntity.ok("User was not registered in Keycloak nor the system");
        }
    }

    @DeleteMapping("/delete")
    //delete user by username
    public ResponseEntity<String> deleteUser(Authentication authentication) {
        // Extract JWT from the Authentication object
        Jwt jwt = (Jwt) authentication.getPrincipal();
        // Extract the username from the JWT claims (typically 'preferred_username' or 'sub')
        String username = jwt.getClaimAsString("preferred_username");
        if (username == null) {
            username = jwt.getSubject(); // Fallback to 'sub' claim if 'preferred_username' is not present
        }
        // Fetch the user details from the database
        User user = userService.findUserByUsername(username);
        // Delete the user from the database
        userService.deleteUserById(user.getId());
        // Delete the user from Keycloak
        keycloakAdminService.deleteUserInKeycloak(username);
        return ResponseEntity.ok("User deleted successfully");
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

