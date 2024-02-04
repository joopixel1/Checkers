package com.pixie.checkers_backend.services.implementations;

import com.pixie.checkers_backend.models.modals.LoginModal;
import com.pixie.checkers_backend.models.modals.SignupModal;
import com.pixie.checkers_backend.services.interfaces.AuthService;
import com.pixie.checkers_backend.services.interfaces.JWTTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JWTTokenService jwtTokenService;
    private final ReactiveAuthenticationManager authManager;
    private final ReactiveUserDetailsService userDetailsService;

    @Override
    public Mono<String> signup(SignupModal signupModal) {
        // TODO
        return userDetailsService.findByUsername("JT").flatMap(jwtTokenService::generateJWTToken);
    }

    @Override
    public Mono<String> login(LoginModal loginModal) {
        return authManager.authenticate(new UsernamePasswordAuthenticationToken(loginModal.getUsername(), loginModal.getPassword()))
                        .then(userDetailsService.findByUsername(loginModal.getUsername()).flatMap(jwtTokenService::generateJWTToken));
    }

}
