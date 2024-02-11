package com.pixie.checkers_backend.repositories;

import com.pixie.checkers_backend.models.entities.Friend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FriendRepository extends ReactiveCrudRepository<Friend, String> {

    Mono<Friend> findByInitializerIsAndAccepterIs(String initializer, String accepter);
    Flux<Friend> findAllByInitializerIsOrAccepterIs(String initializer, String accepter);
    Flux<Friend> findAllByAccepterIsAndCompleteNot(String accepter);

}
