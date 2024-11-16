package com.finance.validation;


import com.finance.validation.impl.KeyInformationKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = KeyInformationKindValidation.class)
@Documented
public @interface KeyInformationKind {
    boolean allowNull() default false;

    String message() default "Key information kind invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
