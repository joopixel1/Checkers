package com.pixie.checkers_backend.annotations;

import com.pixie.checkers_backend.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
public class UserValidator implements ConstraintValidator<ValidUser, String> {

    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return userRepository.existsById(s).subscribeOn(Schedulers.boundedElastic()).toFuture().get();
    }

}
