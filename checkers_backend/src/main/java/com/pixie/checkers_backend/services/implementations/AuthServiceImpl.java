package com.pixie.checkers_backend.services.implementations;

import com.pixie.checkers_backend.models.entities.User;
import com.pixie.checkers_backend.models.modals.LoginModal;
import com.pixie.checkers_backend.models.modals.SignupModal;
import com.pixie.checkers_backend.services.interfaces.AuthService;
import com.pixie.checkers_backend.services.interfaces.JWTTokenService;
import com.pixie.checkers_backend.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTTokenService jwtTokenService;
    private final ReactiveAuthenticationManager authManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Mono<String> signup(SignupModal modal) {
        return userService.createUser(new User(
                modal.getUsername(), passwordEncoder.encode(modal.getPassword()), modal.getEmail(),
                true, 1000, modal.getCountry(), modal.getLanguage(), modal.getImage(), new ArrayList<>()
        )).flatMap(jwtTokenService::generateJWTToken);
    }

    @Override
    public Mono<String> login(LoginModal loginModal) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(loginModal.getUsername(), loginModal.getPassword()))
                        .then(userService.findByUsername(loginModal.getUsername()).flatMap(jwtTokenService::generateJWTToken));
    }

}
