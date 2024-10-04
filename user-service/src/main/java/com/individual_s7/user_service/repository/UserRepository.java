package com.individual_s7.user_service.repository;

import com.individual_s7.user_service.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email);

    @Transactional
    @Modifying
    @Query("UPDATE User user SET user.email = :email, user.username = :username, user.bio = :bio, user.profile = :profile WHERE user.id = :id")
    int updatePerson(@Param("id") Long id, @Param("email") String email, @Param("username") String username, @Param("bio") String bio, @Param("profile") String profile);
}