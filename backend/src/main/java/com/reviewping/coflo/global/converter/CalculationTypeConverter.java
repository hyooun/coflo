package com.reviewping.coflo.global.converter;

import com.reviewping.coflo.domain.project.domain.CalculationType;
import org.springframework.core.convert.converter.Converter;

public class CalculationTypeConverter implements Converter<String, CalculationType> {

    @Override
    public CalculationType convert(String source) {
        return CalculationType.of(source);
    }
}
