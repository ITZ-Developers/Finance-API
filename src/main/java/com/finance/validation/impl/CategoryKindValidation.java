package com.finance.validation.impl;

import com.finance.constant.FinanceConstant;
import com.finance.validation.CategoryKind;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CategoryKindValidation implements ConstraintValidator<CategoryKind, Integer> {
    private boolean allowNull;

    @Override
    public void initialize(CategoryKind constraintAnnotation) {
        allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(Integer kind, ConstraintValidatorContext constraintValidatorContext) {
        if (kind == null && allowNull) {
            return true;
        }
        if (!Objects.equals(kind, FinanceConstant.CATEGORY_KIND_INCOME) &&
                !Objects.equals(kind, FinanceConstant.CATEGORY_KIND_EXPENDITURE)) {
            return false;
        }
        return true;
    }
}
