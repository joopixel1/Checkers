package com.pixie.checkers_backend.services.interfaces;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

public interface JWTTokenService {

    public Mono<String> generateJWTToken(UserDetails userDetails);
    public Mono<UserDetails> validateJwtToken(String token);

}
