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
    String id;
    @ValidUser String user1;
    @ValidUser String user2;
    Date since;

}