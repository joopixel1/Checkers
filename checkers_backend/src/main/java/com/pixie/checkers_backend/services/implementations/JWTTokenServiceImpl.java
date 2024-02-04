package com.pixie.checkers_backend.services.implementations;

import com.pixie.checkers_backend.services.interfaces.JWTTokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class JWTTokenServiceImpl implements JWTTokenService {

    private final ReactiveUserDetailsService userDetailsService;

    @Value("${checkers.secret}")
    private String jwtSecret;

    @Override
    public Mono<String> generateJWTToken(UserDetails userDetails) {
        return getSigninkey().map(k ->
                Jwts.builder().setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername()).setIssuedAt(new Date())
                .signWith(k, SignatureAlgorithm.HS512).compact()
        );
    }

    @Override
    public Mono<UserDetails> validateJwtToken(String token) {
        return getUsernameFromToken(token).flatMap(userDetailsService::findByUsername);
    }

    private Mono<String> getUsernameFromToken(String token) {
        return getSigninkey().map(k ->
                Jwts.parserBuilder().setSigningKey(k).build()
                .parseClaimsJws(token).getBody().getSubject()
        );
    }

    private Mono<Key> getSigninkey() {
        byte[] key_byte = Decoders.BASE64.decode(jwtSecret);
        return Mono.just(Keys.hmacShaKeyFor(key_byte));
    }

}
