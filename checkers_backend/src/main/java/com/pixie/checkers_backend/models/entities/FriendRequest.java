package com.pixie.checkers_backend.models.entities;

import com.pixie.checkers_backend.annotations.ValidUser;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "friend_request")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FriendRequest {

    @Id
    @EqualsAndHashCode.Include
    String id;
    @ValidUser String initializer;
    @ValidUser String accepter;

}
