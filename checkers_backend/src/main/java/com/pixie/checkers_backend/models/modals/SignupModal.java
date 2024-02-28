package com.pixie.checkers_backend.models.modals;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class SignupModal {

    @NonNull
    @NotBlank
    private final String username;
    @NonNull
    @NotBlank
    private final String password;
    @NonNull
    @NotBlank
    @Email
    private final String email;

    private final Boolean online;
    private final String country;
    private final String language;
    private final Byte[] image;

}
