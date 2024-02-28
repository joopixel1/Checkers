package com.pixie.checkers_backend.services.interfaces;

import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.Chat;
import com.pixie.checkers_backend.models.modals.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ChatService {

    Mono<Chat> createChat(String username, ChatModal modal);
    Flux<Chat> readChat(String friendID, Integer page);
    Mono<Chat> updateChat(String username, ChatUpdateModal modal);
    Mono<Chat> deleteChat(String username, String chatID);

    Mono<FriendDTO> subscribeChat(String username, String chatID);

    Mono<FriendDTO> unsubscribeChat(String username, String chatID);
}