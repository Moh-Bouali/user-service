package com.individual_s7.user_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private String username;
    private String bio;
    private String profile;

}
