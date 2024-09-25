package com.individual_s7.user_service.mapper;

import com.individual_s7.user_service.dto.UserRequest;
import com.individual_s7.user_service.model.User;
import com.individual_s7.user_service.dto.UserResponse;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getBio(), user.getProfile());
    }

    public static User toUser(UserRequest userRequest) {
        return new User(userRequest.id(), userRequest.username(), userRequest.email(), userRequest.bio(), userRequest.profile());
    }
}

