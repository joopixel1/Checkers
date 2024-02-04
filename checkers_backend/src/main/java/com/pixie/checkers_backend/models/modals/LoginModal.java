package com.pixie.checkers_backend.models.modals;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
public class LoginModal {
    @NonNull @NotBlank private String username;
    @NonNull @NotBlank private String password;
}
