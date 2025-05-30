package com.david.auth_layer_architecture.business.validators.annotation;

import com.david.auth_layer_architecture.business.validators.validation.HasAccessTokenToChangePasswordValidation;
import com.david.auth_layer_architecture.presentation.messages.CredentialMessages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = HasAccessTokenToChangePasswordValidation.class)
public @interface HasAccessTokenToChangePassword {
    String message() default CredentialMessages.ALREADY_HAVE_ACCESS_TOKEN_TO_CHANGE_PASSWORD;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
