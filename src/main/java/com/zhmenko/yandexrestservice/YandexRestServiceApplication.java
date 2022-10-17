package com.zhmenko.yandexrestservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@OpenAPIDefinition
@EnableTransactionManagement
@EnableJpaAuditing
public class YandexRestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YandexRestServiceApplication.class, args);
    }
}
