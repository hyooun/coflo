package com.reviewping.coflo.global.converter;

import com.reviewping.coflo.domain.project.enums.ScoreDisplayType;
import org.springframework.core.convert.converter.Converter;

public class ScoreDisplayTypeConverter implements Converter<String, ScoreDisplayType> {

    @Override
    public ScoreDisplayType convert(String source) {
        return ScoreDisplayType.of(source);
    }
}
