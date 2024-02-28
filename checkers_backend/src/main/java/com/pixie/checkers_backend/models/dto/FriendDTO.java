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
    private Friend.UserInfo friend;

    public static FriendDTO mapToFriendDTO(Friend friend, String username){
        return new FriendDTO(friend.getId(), (friend.getUser1().getUser().equals(username)) ? friend.getUser2() : friend.getUser1());
    }

}
