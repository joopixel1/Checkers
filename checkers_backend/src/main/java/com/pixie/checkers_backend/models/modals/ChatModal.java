package com.pixie.checkers_backend.models.modals;

import com.pixie.checkers_backend.annotations.ValidFriend;
import com.pixie.checkers_backend.annotations.ValidUser;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class ChatModal {

    @NotBlank
    @NonNull
    final private String content;
    @ValidFriend
    @NonNull
    private final String friendID;

}
