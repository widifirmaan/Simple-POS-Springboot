package com.primaelectronic.pos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(new StringToLocalDateTimeConverter()));
    }

    static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        @Override
        public LocalDateTime convert(@NonNull String source) {
            try {
                // Try standard ISO first
                return LocalDateTime.parse(source);
            } catch (Exception e) {
                // Try custom format
                try {
                    return LocalDateTime.parse(source, FORMATTER);
                } catch (Exception ex) {
                    // Fail or return null? Better to fail or handle gracefully.
                    // For now, let's strictly try to parse or throw.
                    throw ex;
                }
            }
        }
    }
}
