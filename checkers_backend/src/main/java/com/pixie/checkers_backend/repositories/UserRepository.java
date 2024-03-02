package com.pixie.checkers_backend.repositories;

import com.pixie.checkers_backend.models.entities.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {

    Flux<User> findAllByUsernameLikeIgnoreCase(String search, Pageable page);

}
