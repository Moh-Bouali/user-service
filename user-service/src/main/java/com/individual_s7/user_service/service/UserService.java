package com.individual_s7.user_service.service;

import com.individual_s7.user_service.config.RabbitMQConfig;
import com.individual_s7.user_service.dto.UserRequest;
import com.individual_s7.user_service.events.UserUpdatedEvent;
import com.individual_s7.user_service.model.User;
import com.individual_s7.user_service.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void createUser(UserRequest userRequest){
        User user = User.builder()
                .email(userRequest.email())
                .username(userRequest.username())
                .bio(userRequest.bio())
                .profile(userRequest.profile())
                .build();
        System.out.println("Saving user: " + user);
        userRepository.save(user);
    }

    public void deleteUserById(Long id){
        if(userRepository.findById(id).isEmpty()){
            throw new RuntimeException("User not found");
        }
        else{
            rabbitTemplate.convertAndSend(RabbitMQConfig.USER_DELETE_EXCHANGE,
                    RabbitMQConfig.USER_DELETE_ROUTING_KEY, id);
            userRepository.deleteById(id);
        }
    }
    @Transactional
    public void updateUserById(UserRequest userRequest){
        if(userRepository.findById(userRequest.id()).isEmpty()){
            throw new RuntimeException("User not found");
        }else{
            User newUser = User.builder()
                    .id(userRequest.id())
                    .email(userRequest.email())
                    .username(userRequest.username())
                    .bio(userRequest.bio())
                    .profile(userRequest.profile())
                    .build();
            userRepository.updatePerson(newUser.getId(),newUser.getEmail(), newUser.getUsername(), newUser.getBio(), newUser.getProfile());
            UserUpdatedEvent userUpdateEvent = UserUpdatedEvent.builder()
                    .id(newUser.getId())
                    .username(newUser.getUsername())
                    .build();
            rabbitTemplate.convertAndSend(RabbitMQConfig.USER_UPDATE_EXCHANGE,
                    RabbitMQConfig.USER_UPDATE_ROUTING_KEY, userUpdateEvent);
        }
    }

    public User findUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    public List<User> findUserByUsernameOrEmail(String query){
        return userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query);
    }

    public boolean userExists(String username, String email){
        Optional<User> user = userRepository.findByUsernameOrEmail(username, email);
        return user.isPresent();
    }
}
