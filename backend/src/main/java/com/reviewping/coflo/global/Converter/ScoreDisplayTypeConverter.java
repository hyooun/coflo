package com.reviewping.coflo.global.Converter;

import com.reviewping.coflo.domain.project.domain.ScoreDisplayType;
import org.springframework.core.convert.converter.Converter;

public class ScoreDisplayTypeConverter implements Converter<String, ScoreDisplayType> {

    @Override
    public ScoreDisplayType convert(String source) {
        return ScoreDisplayType.of(source);
    }
}
