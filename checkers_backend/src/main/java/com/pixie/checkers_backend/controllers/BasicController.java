package com.pixie.checkers_backend.controllers;

import jakarta.servlet.annotation.WebFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1")
public class BasicController {

    @Value("${checkers.version}")
    private String version;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<String> getApi(){
        return Mono.just("Hello!! Api Under Development!!");
    }

    @GetMapping("version")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<String> getVersion(){
        return Mono.just("Version: v1");
    }

    @GetMapping("author")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Mono<String> getAuthor(){
        return Mono.just("Author: joopixel1");
    }

}
