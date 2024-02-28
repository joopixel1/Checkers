package com.pixie.checkers_backend.models.entities;

import com.pixie.checkers_backend.annotations.ValidFriend;
import com.pixie.checkers_backend.annotations.ValidUser;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(value = "chat")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private ChatType type;
    private Date timeStamp;
    private String content;
    private String sender;
    private String friendID;


    public enum ChatType {
        CHAT, JOIN, DELETED, LEAVE
    }

}
