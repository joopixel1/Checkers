package com.pixie.checkers_backend.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

@Target( { FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FriendValidator.class)
public @interface ValidFriend {

    // error message
    public String message() default "Invalid friendID: FriendID doesnt exist";
    // represents group of constraints
    public Class<?>[] groups() default {};
    // represents additional information about annotation
    public Class<? extends Payload>[] payload() default {};

}
