package com.loko.config;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

@Configuration
public class JacksonConfig {
    private static final Logger logger = LoggerFactory.getLogger(JacksonConfig.class);
    @Value("${spring.jackson.date-format}")
    private String DEFAULT_FORMAT_DATE_PATTERN;

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
       logger.info("DEFAULT_FORMAT_DATE_PATTERN " + DEFAULT_FORMAT_DATE_PATTERN);
        return builder -> {
            LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(
                    DateTimeFormatter.ofPattern(DEFAULT_FORMAT_DATE_PATTERN));
            builder.serializers(localDateTimeSerializer);
        };
    }
}
