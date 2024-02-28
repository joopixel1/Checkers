package com.pixie.checkers_backend.annotations;

import jakarta.validation.*;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target( { FIELD, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserValidator.class)
public @interface ValidUser {

    // error message
    public String message() default "Invalid user: User does not exist";
    // represents group of constraints
    public Class<?>[] groups() default {};
    // represents additional information about annotation
    public Class<? extends Payload>[] payload() default {};

}
