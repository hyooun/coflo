package com.reviewping.coflo.global.config;

import com.reviewping.coflo.global.converter.CalculationTypeConverter;
import com.reviewping.coflo.global.converter.ScoreDisplayTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new CalculationTypeConverter());
        registry.addConverter(new ScoreDisplayTypeConverter());
    }
}
