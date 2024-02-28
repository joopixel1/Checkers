package com.pixie.checkers_backend.models.entities;

import com.pixie.checkers_backend.annotations.ValidUser;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

@Document(value = "friend")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Friend {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private UserInfo user1;
    private UserInfo user2;
    private Date since;

    @Data
    @Builder
    @AllArgsConstructor
    public static class UserInfo {
        private String user;
        private Boolean online;
        private Date lastOnline;
    }

}
