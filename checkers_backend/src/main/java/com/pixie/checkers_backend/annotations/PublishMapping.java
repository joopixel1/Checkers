package com.pixie.checkers_backend.annotations;

import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PublishMapping {

    // path message
    public String value() ;
}
