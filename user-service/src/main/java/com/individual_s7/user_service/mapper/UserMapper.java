package com.individual_s7.user_service.mapper;

import com.individual_s7.user_service.dto.UserRequest;
import com.individual_s7.user_service.model.User;
import com.individual_s7.user_service.dto.UserResponse;

public class UserMapper {

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(user.getId(),user.getEmail(), user.getUsername(), user.getBio(), user.getProfile());
    }

    public static User toUser(UserRequest userRequest) {
        return new User(userRequest.id(),userRequest.email(), userRequest.username(), userRequest.bio(), userRequest.profile());
    }
}

