package com.pixie.checkers_backend.services.interfaces;

import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.dto.UserDTO;
import com.pixie.checkers_backend.models.entities.User;
import com.pixie.checkers_backend.models.modals.UserModal;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FriendService {

    public Mono<String> createFriends(String username, String friend);

    public Flux<FriendDTO> readFriends(String username);

    public Flux<FriendDTO> readFriendsPending(String username);

    public Mono<Void> deleteFriend(String username, String friend);

    public Flux<String> search(String username);

}
