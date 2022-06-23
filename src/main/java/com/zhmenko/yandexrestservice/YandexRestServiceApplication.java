package com.zhmenko.yandexrestservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@OpenAPIDefinition
@EnableTransactionManagement
public class YandexRestServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(YandexRestServiceApplication.class, args);
    }
}
