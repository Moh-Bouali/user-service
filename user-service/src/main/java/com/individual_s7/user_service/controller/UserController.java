package com.individual_s7.user_service.controller;

import com.individual_s7.user_service.dto.UserRequest;
import com.individual_s7.user_service.dto.UserResponse;
import com.individual_s7.user_service.mapper.UserMapper;
import com.individual_s7.user_service.model.User;
import com.individual_s7.user_service.service.KeycloakAdminService;
import com.individual_s7.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

        private final UserService userService;
        private final KeycloakAdminService keycloakAdminService;
        @PostMapping("/register")
        public ResponseEntity<String> registerUser(@RequestBody UserRequest userRequest) {
            // Save the user to your local database
            userService.createUser(userRequest);

            // Register the user in Keycloak
            keycloakAdminService.registerUserInKeycloak(userRequest.username(), userRequest.password());
            return ResponseEntity.ok("User created and registered in Keycloak");
        }

    @PutMapping("/update")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest userRequest) {
        // Fetch the current user details from the database
        User currentUser = userService.findUserById(userRequest.id());

//        if (currentUser == null) {
//            return ResponseEntity.notFound().build();
//        }
        // Update the user in your local database
        userService.updateUserById(userRequest);

        // Update the user in Keycloak
        keycloakAdminService.updateUserInKeycloak(currentUser.getUsername(), userRequest.username(), userRequest.email());

        // Fetch the updated user from the database
        User updatedUser = userService.findUserById(userRequest.id());

        UserResponse userResponse = UserMapper.toUserResponse(updatedUser);

        // Return the updated user details in the response
        return ResponseEntity.ok(userResponse);
    }
}
