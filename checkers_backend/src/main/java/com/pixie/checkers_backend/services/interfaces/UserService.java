package com.pixie.checkers_backend.services.interfaces;

import com.pixie.checkers_backend.models.dto.UserDTO;
import com.pixie.checkers_backend.models.entities.User;
import com.pixie.checkers_backend.models.modals.UserModal;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService extends ReactiveUserDetailsService {

    public Mono<User> createUser(User user);

    public Mono<UserDTO> readUser(String username);

    public Flux<UserDTO> readAllUsers();

    public Mono<UserDTO> updateUser(String username, UserModal modal);

    public Mono<Void> deleteUser(String username);

}
