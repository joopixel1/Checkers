package com.pixie.checkers_backend.models.modals;

import com.pixie.checkers_backend.annotations.ValidFriend;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ChatUpdateModal {

    @NotBlank
    @NonNull
    private final String content;
    @NonNull
    private final String chatID;

}
