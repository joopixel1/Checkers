package com.pixie.checkers_backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.pixie.checkers_backend.models.dto.UserDTO;
import com.pixie.checkers_backend.models.modals.UserModal;
import com.pixie.checkers_backend.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public @ResponseBody Mono<UserDTO> getUser(@AuthenticationPrincipal Principal principal){
        return userService.readUser(principal.getName());
    }

    @PutMapping("")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public @ResponseBody Mono<UserDTO> putUser(@AuthenticationPrincipal Principal principal, @RequestBody UserModal modal){
        return userService.updateUser(principal.getName(), modal);
    }

    @PatchMapping(path = "", consumes = "application/json-patch+json")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<UserDTO> patchUser(@AuthenticationPrincipal Principal principal, @RequestBody JsonPatch jsonPatch){
        return userService.patchUser(principal.getName(), jsonPatch);
    }

    @DeleteMapping("")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public @ResponseBody Mono<Void> deleteUser(@AuthenticationPrincipal Principal principal){
        return userService.deleteUser(principal.getName());
    }

}
