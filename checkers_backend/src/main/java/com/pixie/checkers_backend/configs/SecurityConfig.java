package com.pixie.checkers_backend.configs;


import com.pixie.checkers_backend.services.interfaces.JWTTokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;


@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTTokenService jwtTokenService;
    private final ReactiveUserDetailsService reactiveUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable)

                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .anyExchange().permitAll()
                )
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .addFilterBefore(this::filterFunction, SecurityWebFiltersOrder.AUTHENTICATION)
                .authenticationManager(authenticationManager())
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationManager(){
        UserDetailsRepositoryReactiveAuthenticationManager authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    private Mono<Void> filterFunction(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain){
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if(authHeader==null || !authHeader.startsWith("Bearer")) return chain.filter(exchange);

        String token = authHeader.substring(authHeader.indexOf(' ')+1);
        Mono<UserDetails> userDetails = jwtTokenService.validateJwtToken(token);

        return userDetails
                .zipWith(userDetails.map(ud -> true).defaultIfEmpty(false))
                .flatMap(tuple -> {
                    boolean userDetailsPresent = tuple.getT2();

                    if (userDetailsPresent) {
                        UserDetails ud = tuple.getT1();
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                        authenticationToken.setDetails(exchange.getRequest());
                        return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authenticationToken));
                    } else {
                        return chain.filter(exchange);
                    }
                });
    }


}
