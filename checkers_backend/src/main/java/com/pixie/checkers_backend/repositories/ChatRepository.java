package com.pixie.checkers_backend.repositories;

import com.pixie.checkers_backend.models.entities.Chat;
import com.pixie.checkers_backend.models.entities.Friend;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatRepository extends ReactiveCrudRepository<Chat, String> {

        Flux<Chat> findAllByFriendIDIsOrderByTimeStampAsc(String friendID, Pageable page);

}
