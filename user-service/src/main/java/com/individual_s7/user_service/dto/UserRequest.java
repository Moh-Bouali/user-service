package com.individual_s7.user_service.dto;

public record UserRequest(Long id, String email, String password, String username, String bio, String profile) {
}
