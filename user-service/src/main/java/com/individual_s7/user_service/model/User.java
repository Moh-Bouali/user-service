package com.individual_s7.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "t_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String username;
    private String bio;
    @Column(name = "profile")
    private String profile;
}
