package com.pixie.checkers_backend.controllers;

import com.pixie.checkers_backend.models.dto.UserDTO;
import com.pixie.checkers_backend.models.modals.UserModal;
import com.pixie.checkers_backend.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<UserDTO> getUser(@AuthenticationPrincipal Mono<Principal> principal){
        return principal.map(Principal::getName).flatMap(userService::readUser);
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<UserDTO> putUser(@AuthenticationPrincipal Mono<Principal> principal, @RequestBody UserModal modal){
        return principal.map(Principal::getName).flatMap(u -> userService.updateUser(u, modal));
    }

    @PatchMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<String> patchUser(){
        // TODO
        return Mono.just("Version: v1");
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody Mono<Void> deleteUser(@AuthenticationPrincipal Mono<Principal> principal){
        return principal.map(Principal::getName).flatMap(userService::deleteUser);
    }

}
