package com.pixie.checkers_backend.services.interfaces;

import com.pixie.checkers_backend.models.modals.LoginModal;
import com.pixie.checkers_backend.models.modals.SignupModal;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

public interface AuthService {

    public Mono<String> signup(SignupModal signupModal);
    public Mono<String> login(LoginModal loginModal);

}
