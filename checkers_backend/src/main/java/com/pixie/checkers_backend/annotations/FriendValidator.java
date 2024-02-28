package com.pixie.checkers_backend.annotations;

import com.pixie.checkers_backend.repositories.FriendRepository;
import com.pixie.checkers_backend.repositories.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
public class FriendValidator implements ConstraintValidator<ValidFriend, String> {

    private final FriendRepository friendRepository;

    @SneakyThrows
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return ReactiveSecurityContextHolder.getContext()
                .flatMap(securityContext -> {
                    Authentication authentication = securityContext.getAuthentication();
                    return authentication != null ? Mono.just(authentication.getName()) : Mono.empty();
                })
                .flatMap(u -> friendRepository.findById(s).map(f -> f.getUser1().equals(u) || f.getUser2().equals(u)))
                .defaultIfEmpty(false)
                .subscribeOn(Schedulers.boundedElastic()).toFuture().get();
    }

}
