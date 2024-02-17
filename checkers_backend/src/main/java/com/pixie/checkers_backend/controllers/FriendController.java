package com.pixie.checkers_backend.controllers;

import com.pixie.checkers_backend.annotations.ValidUser;
import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.FriendRequest;
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

    @GetMapping("/pending")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Flux<FriendRequest> getFriendsPending(@AuthenticationPrincipal Mono<Principal> principal){
        return principal.map(Principal::getName).flatMapMany(friendService::readFriendRequestsPending);
    }

    @GetMapping("/waiting")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Flux<FriendRequest> getFriendsWaiting(@AuthenticationPrincipal Mono<Principal> principal){
        return principal.map(Principal::getName).flatMapMany(friendService::readFriendRequestsWaiting);
    }

    @PostMapping("/request/{friend}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Mono<FriendRequest> postFriendRequest(@AuthenticationPrincipal Mono<Principal> principal, @PathVariable @ValidUser String friend){
        return principal.map(Principal::getName).flatMap(u -> friendService.createFriendRequest(u, friend));
    }

    @PostMapping("/{friendId}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Mono<Void> postFriend(@AuthenticationPrincipal Mono<Principal> principal, @PathVariable String friendId){
        return principal.map(Principal::getName).flatMap(u -> friendService.createFriend(u, friendId));
    }

    @DeleteMapping("/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody Mono<Void> deleteFriend(@PathVariable String friendId){
        return friendService.deleteFriend(friendId);
    }

}
