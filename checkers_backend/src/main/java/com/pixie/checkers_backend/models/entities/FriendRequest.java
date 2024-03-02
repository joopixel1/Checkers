package com.pixie.checkers_backend.models.entities;

import com.pixie.checkers_backend.annotations.ValidUser;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(value = "friend_request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FriendRequest {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String initializer;
    private String accepter;
    private Date requestDate;

}
