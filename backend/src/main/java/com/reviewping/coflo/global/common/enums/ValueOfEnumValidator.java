package com.reviewping.coflo.global.common.enums;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValueOfEnumValidator implements ConstraintValidator<EnumValue, String> {

    private EnumValue enumValue;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        this.enumValue = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Enum<?>[] enumValues = this.enumValue.enumClass().getEnumConstants();

        if (enumValues == null) return false;

        if (value == null) return false;

        return Arrays.stream(enumValues).anyMatch(eVal -> isValueValid(value, eVal));
    }

    private boolean isValueValid(String value, Enum<?> eVal) {
        return value.equals(eVal.toString())
                || (this.enumValue.ignoreCase() && value.equalsIgnoreCase(eVal.toString()));
    }
}
