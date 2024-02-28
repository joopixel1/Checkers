package com.pixie.checkers_backend.repositories;

import com.pixie.checkers_backend.models.entities.Friend;
import com.pixie.checkers_backend.models.entities.FriendRequest;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FriendRequestRepository extends ReactiveCrudRepository<FriendRequest, String> {

    Flux<FriendRequest> findAllByInitializerIs(String initializer);
    Flux<FriendRequest> findAllByAccepterIs(String accepter);
    Mono<Boolean> existsByInitializerIsAndAccepterIs(String initializer, String accepter);

}
