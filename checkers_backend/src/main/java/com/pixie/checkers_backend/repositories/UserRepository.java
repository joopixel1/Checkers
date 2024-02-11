package com.pixie.checkers_backend.repositories;

import com.pixie.checkers_backend.models.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
}
