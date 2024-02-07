package com.pixie.checkers_backend.models.modals;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
public class SignupModal {

    @NonNull @NotBlank private final String username;
    @NonNull @NotBlank private final String password;
    @NonNull @NotBlank @Email private final String email;

    private Boolean online;
    private String country;
    private String language;
    private Byte[] image;

}
