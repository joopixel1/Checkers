package com.pixie.checkers_backend.services.implementations;

import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.Friend;
import com.pixie.checkers_backend.repositories.FriendRepository;
import com.pixie.checkers_backend.services.interfaces.FriendService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class FriendServiceImpl implements FriendService {

    FriendRepository friendRepository;

    @Override
    public Mono<String> createFriends(String username, String friend) {
        if (username.equals(friend)) return Mono.error(new RuntimeException("Username and friend must be different"));

        return friendRepository.findByInitializerIsAndAccepterIs(username, friend)
                .switchIfEmpty(friendRepository.findByInitializerIsAndAccepterIs(friend, username))
                .flatMap(f -> {
                    if(f == null) {
                        Friend nf = new Friend(null, username, friend, false, null);
                        return friendRepository.save(nf).then(Mono.just("Friend Request Sent!"));
                    }
                    else {
                        f.setComplete(true);
                        f.setSince(new Date());
                        return friendRepository.save(f).then(Mono.just("Friend Request Accepted!"));
                    }
                });
    }

    @Override
    public Flux<FriendDTO> readFriends(String username) {
        return friendRepository.findAllByInitializerIsOrAccepterIs(username, username)
                .filter(Friend::getComplete)
                .map(it -> FriendDTO.mapToFriendDTO(it, username));
    }

    @Override
    public Flux<FriendDTO> readFriendsPending(String username) {
        return friendRepository.findAllByAccepterIsAndCompleteNot(username)
                .map(it -> FriendDTO.mapToFriendDTO(it, username));
    }

    @Override
    public Mono<Void> deleteFriend(String username, String friend) {
        return friendRepository.findByInitializerIsAndAccepterIs(username, friend)
                .switchIfEmpty(friendRepository.findByInitializerIsAndAccepterIs(friend, username))
                .flatMap(f -> friendRepository.deleteById(f.getId()));
    }

    @Override
    public Flux<String> search(String username) {
        // TODO
        return null;
    }

}
