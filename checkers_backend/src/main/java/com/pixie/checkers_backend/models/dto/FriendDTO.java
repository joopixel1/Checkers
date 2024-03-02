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
    private Friend.UserInfo friendInfo;

    public static FriendDTO mapToFriendDTO(Friend friend, String username){
        return (friend.getUser1().equals(username)) ?
                (new FriendDTO(friend.getId(), friend.getUser2(), friend.getUserInfo2())) :
                (new FriendDTO(friend.getId(), friend.getUser1(), friend.getUserInfo1()));
    }

}
