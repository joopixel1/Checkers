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
    private String user1;
    private String user2;
    private UserInfo userInfo1;
    private UserInfo userInfo2;
    private Date since;

    @Data
    @Builder
    @AllArgsConstructor
    public static class UserInfo {
        private Boolean online;
        private Date lastOnline;
    }

}
