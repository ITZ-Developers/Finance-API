package com.finance.validation;

import com.finance.validation.impl.CategoryKindValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryKindValidation.class)
@Documented
public @interface CategoryKind {
    boolean allowNull() default false;
    String message() default "Category kind invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
