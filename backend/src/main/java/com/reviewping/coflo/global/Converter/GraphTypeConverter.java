package com.reviewping.coflo.global.Converter;

import com.reviewping.coflo.domain.project.entity.GraphType;
import org.springframework.core.convert.converter.Converter;

public class GraphTypeConverter implements Converter<String, GraphType> {

    @Override
    public GraphType convert(String source) {
        return GraphType.of(source);
    }
}
