package com.individual_s7.user_service.events;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdatedEvent implements Serializable {
    private Long id;
    private String username;
}
