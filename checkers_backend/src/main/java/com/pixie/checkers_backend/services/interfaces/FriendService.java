package com.pixie.checkers_backend.services.interfaces;

import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.dto.UserDTO;
import com.pixie.checkers_backend.models.entities.FriendRequest;
import com.pixie.checkers_backend.models.entities.User;
import com.pixie.checkers_backend.models.modals.UserModal;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FriendService {

    public Mono<Void> createFriend(String username, String friendId);

    Mono<FriendRequest> createFriendRequest(String username, String friend);

    public Flux<FriendDTO> readFriends(String username);

    public Flux<FriendRequest> readFriendRequestsPending(String username);

    public Flux<FriendRequest> readFriendRequestsWaiting(String username);

    public Mono<Void> deleteFriend(String friendId);

    public Flux<String> search(String username);

}
