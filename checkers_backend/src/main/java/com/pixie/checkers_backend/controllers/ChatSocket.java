package com.pixie.checkers_backend.controllers;

import com.pixie.checkers_backend.annotations.*;
import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.Chat;
import com.pixie.checkers_backend.models.modals.ChatModal;
import com.pixie.checkers_backend.services.interfaces.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.security.Principal;

@SocketController("/chat")
@RequiredArgsConstructor
public class ChatSocket {

    private final ChatService chatService;

    @PublishMapping("/add")
    public Mono<Chat> addChat(@SocketPrincipal String principal, @SocketPayload ChatModal modal){
        return chatService.createChat(principal, modal);
    }

    @PublishMapping("/delete")
    public Mono<Chat> deleteChat(@SocketPrincipal String principal, @SocketPayload String chatID){
        return chatService.deleteChat(principal, chatID);
    }

    @SubscribeMapping("")
    public Mono<FriendDTO> subscribeChat(@SocketPrincipal String principal, @SocketTopic String friendID){
        return chatService.subscribeChat(principal, friendID.substring(friendID.lastIndexOf('/')+1));
    }

    @UnSubscribeMapping("")
    public Mono<FriendDTO> unsubscribeChat(@SocketPrincipal String principal, @SocketTopic String friendID){
        return chatService.unsubscribeChat(principal, friendID.substring(friendID.lastIndexOf('/')+1));
    }




}
