package com.finance.validation.impl;

import com.finance.constant.FinanceConstant;
import com.finance.validation.StatusConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class StatusValidation implements ConstraintValidator<StatusConstraint, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(StatusConstraint constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null && allowNull) {
            return true;
        }
        if (!Objects.equals(value, FinanceConstant.STATUS_ACTIVE) &&
                !Objects.equals(value, FinanceConstant.STATUS_PENDING) &&
                !Objects.equals(value, FinanceConstant.STATUS_LOCK)) {
            return false;
        }
        return true;
    }
}