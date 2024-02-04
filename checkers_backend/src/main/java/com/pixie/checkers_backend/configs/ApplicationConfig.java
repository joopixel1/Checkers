package com.pixie.checkers_backend.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.adapter.WebHttpHandlerBuilder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    @Bean
    public ReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user").password(passwordEncoder().encode("password"))
                .roles("USER").build();
        UserDetails admin = User.withUsername("admin").password(passwordEncoder().encode("admin"))
                .roles("USER").build();
        return new MapReactiveUserDetailsService(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
