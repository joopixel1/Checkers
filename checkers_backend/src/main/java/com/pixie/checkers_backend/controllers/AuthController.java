package com.pixie.checkers_backend.controllers;

import com.pixie.checkers_backend.models.modals.LoginModal;
import com.pixie.checkers_backend.models.modals.SignupModal;
import com.pixie.checkers_backend.services.interfaces.AuthService;
import com.pixie.checkers_backend.services.interfaces.JWTTokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JWTTokenService jwtTokenService;
    private final AuthService authService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<String> login(@Valid @RequestBody LoginModal modal){
        return authService.login(modal);
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<String> signup(@Valid @RequestBody SignupModal modal){
        return authService.signup(modal);
    }

}
