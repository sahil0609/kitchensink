package com.sahil.kitchensink.validations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueMemberEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface UniqueMemberEmail {

    public String message() default "There is already member with this email!";

    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default{};
}
