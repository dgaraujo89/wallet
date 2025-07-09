package com.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

@Configuration
public class GeneralConfiguration {

    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        final var processor = new MethodValidationPostProcessor();
        processor.setAdaptConstraintViolations(true);
        return processor;
    }

}
