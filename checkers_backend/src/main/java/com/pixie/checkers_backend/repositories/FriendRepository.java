package com.pixie.checkers_backend.repositories;

import com.pixie.checkers_backend.models.entities.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.lang.NonNull;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

public interface FriendRepository extends ReactiveCrudRepository<Friend, String> {

    Mono<Boolean> existsByUser1IsAndUser2Is(String user1, String user2);

}
