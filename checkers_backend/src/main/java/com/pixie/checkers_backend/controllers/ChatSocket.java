package com.pixie.checkers_backend.controllers;

import com.pixie.checkers_backend.annotations.*;
import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.Chat;
import com.pixie.checkers_backend.models.modals.ChatModal;
import com.pixie.checkers_backend.services.interfaces.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import reactor.core.publisher.Mono;

@SocketController("/chat")
@RequiredArgsConstructor
public class ChatSocket {

    private final ChatService chatService;

    @PublishMapping("/add")
    public Mono<Chat> addChat(@SocketAuthentication Mono<Authentication> principal, @SocketPayload ChatModal modal){
        return principal.flatMap(p -> chatService.createChat(p.getName(), modal));
    }

    @PublishMapping("/delete")
    public Mono<Chat> deleteChat(@SocketAuthentication Mono<Authentication> principal, @SocketPayload String chatID){
        return principal.flatMap(p -> chatService.deleteChat(p.getName(), chatID));
    }

    @SubscribeMapping("")
    public Mono<FriendDTO> subscribeChat(@SocketAuthentication Mono<Authentication> principal, @SocketTopic String friendID){
        return principal.flatMap(p -> chatService.subscribeChat(p.getName(), friendID.substring(friendID.lastIndexOf('/')+1)));
    }

    @UnSubscribeMapping("")
    public Mono<FriendDTO> unsubscribeChat(@SocketAuthentication String principal, @SocketTopic String friendID){
        return chatService.unsubscribeChat(principal, friendID.substring(friendID.lastIndexOf('/')+1));
    }




}
