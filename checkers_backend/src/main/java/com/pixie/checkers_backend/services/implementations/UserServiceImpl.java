package com.pixie.checkers_backend.services.implementations;

import com.pixie.checkers_backend.models.dto.UserDTO;
import com.pixie.checkers_backend.models.entities.User;
import com.pixie.checkers_backend.models.modals.UserModal;
import com.pixie.checkers_backend.repositories.UserRepository;
import com.pixie.checkers_backend.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public Mono<User> createUser(User user) {
        return userRepository.existsById(user.getUsername()).flatMap(it -> {
            if(it)  return Mono.error(new RuntimeException("User with the same username already exists."));
            else return userRepository.save(user);
        });
    }

    @Override
    public Mono<UserDTO> readUser(String username) {
        return userRepository.findById(username).map(UserDTO::mapToUserDTO);
    }

    @Override
    public Flux<UserDTO> readAllUsers() {
        return userRepository.findAll().map(UserDTO::mapToUserDTO);
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return userRepository.findById(username).map(UserDetails.class::cast);
    }

    @Override
    public Mono<UserDTO> updateUser(String username, UserModal modal) {
        return userRepository.findById(username)
                .switchIfEmpty(Mono.empty())
                .flatMap(user -> {
                    if(modal.getOnline() != null) user.setOnline(modal.getOnline());
                    if(modal.getCountry() != null) user.setCountry(modal.getCountry());
                    if(modal.getLanguage() != null) user.setLanguage(modal.getLanguage());
                    if(modal.getImage() != null) user.setImage(modal.getImage());
                    return userRepository.save(user).map(UserDTO::mapToUserDTO);
                });
    }

    @Override
    public Mono<Void> deleteUser(String username) {
        return userRepository.deleteById(username);
    }


}
