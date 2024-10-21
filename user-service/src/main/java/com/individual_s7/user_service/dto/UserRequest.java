package com.individual_s7.user_service.dto;

public record UserRequest(Long id, String email, String username, String password , String bio, String profile) {
}
