package com.pixie.checkers_backend.models.dto;

import com.pixie.checkers_backend.models.entities.Friend;
import com.pixie.checkers_backend.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FriendDTO {

    @EqualsAndHashCode.Include
    private String id;
    private String friend;

    public static FriendDTO mapToFriendDTO(Friend friend, String username){
        return new FriendDTO(friend.getId(), (friend.getInitializer().equals(username)) ? friend.getAccepter() : friend.getInitializer());
    }

}
