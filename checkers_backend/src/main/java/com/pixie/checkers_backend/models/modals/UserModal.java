package com.pixie.checkers_backend.models.modals;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class UserModal {

    private final Boolean online;
    private final String country;
    private final String language;
    private final Byte[] image;

}

