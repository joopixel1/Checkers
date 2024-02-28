package com.pixie.checkers_backend.controllers;

import com.pixie.checkers_backend.annotations.ValidFriend;
import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.Chat;
import com.pixie.checkers_backend.services.interfaces.ChatService;
import com.pixie.checkers_backend.services.interfaces.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/{friendId}/{page}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Flux<Chat> getFriends(@PathVariable @ValidFriend String friendId, @PathVariable Integer page){
        return chatService.readChat(friendId, page);
    }

}
