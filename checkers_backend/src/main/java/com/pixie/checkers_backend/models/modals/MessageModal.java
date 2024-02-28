package com.pixie.checkers_backend.models.modals;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public class MessageModal {

    @NonNull
    private final MessageType type;
    @NonNull
    @NotBlank
    private final String path;
    private final String topic;
    private final String payload;

    public enum MessageType {
        PUBLISH, SUBSCRIBE, UNSUBSCRIBE
    }

}
