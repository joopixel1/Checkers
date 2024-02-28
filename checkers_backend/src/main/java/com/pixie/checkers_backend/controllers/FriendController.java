package com.pixie.checkers_backend.controllers;

import com.pixie.checkers_backend.annotations.ValidFriend;
import com.pixie.checkers_backend.annotations.ValidUser;
import com.pixie.checkers_backend.models.dto.FriendDTO;
import com.pixie.checkers_backend.models.entities.FriendRequest;
import com.pixie.checkers_backend.services.interfaces.FriendService;
import lombok.NoArgsConstructor;
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
    public @ResponseBody Flux<FriendDTO> getFriends(@AuthenticationPrincipal Principal principal){
        return friendService.readFriends(principal.getName());
    }

    @GetMapping("/pending")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Flux<FriendRequest> getFriendsPending(@AuthenticationPrincipal Principal principal){
        return friendService.readFriendRequestsPending(principal.getName());
    }

    @GetMapping("/waiting")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Flux<FriendRequest> getFriendsWaiting(@AuthenticationPrincipal Principal principal){
        return friendService.readFriendRequestsWaiting(principal.getName());
    }

    @PostMapping("/request/{friend}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Mono<FriendRequest> postFriendRequest(@AuthenticationPrincipal Principal principal, @PathVariable @ValidUser String friend){
        return friendService.createFriendRequest(principal.getName(), friend);
    }

    @PostMapping("/{requestId}")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Mono<Void> postFriend(@AuthenticationPrincipal Principal principal, @PathVariable String requestId){
        return friendService.createFriend(principal.getName(), requestId);
    }

    @DeleteMapping("/{friendId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody Mono<Void> deleteFriend(@PathVariable @ValidFriend String friendId){
        return friendService.deleteFriend(friendId);
    }

}
