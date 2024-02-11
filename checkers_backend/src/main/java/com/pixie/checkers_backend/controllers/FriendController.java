package com.pixie.checkers_backend.controllers;

import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.services.interfaces.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/friend")
@RequiredArgsConstructor
public class FriendController {

    private final FriendService friendService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Flux<FriendDTO> getFriends(@AuthenticationPrincipal Mono<Principal> principal){
        return principal.map(Principal::getName).flatMapMany(friendService::readFriends);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Flux<FriendDTO> getFriendsPending(@AuthenticationPrincipal Mono<Principal> principal){
        return principal.map(Principal::getName).flatMapMany(friendService::readFriendsPending);
    }

    @PutMapping("/{friend}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Mono<String> putFriend(@AuthenticationPrincipal Mono<Principal> principal, @PathVariable String friend){
        return principal.map(Principal::getName).flatMap(u -> friendService.createFriends(u, friend));
    }

    @DeleteMapping("/{friend}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody Mono<Void> deleteFriend(@AuthenticationPrincipal Mono<Principal> principal, @PathVariable String friend){
        return principal.map(Principal::getName).flatMap(u -> friendService.deleteFriend(u, friend));
    }

}
