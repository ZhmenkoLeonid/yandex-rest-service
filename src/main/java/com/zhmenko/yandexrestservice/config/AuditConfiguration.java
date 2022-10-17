package com.zhmenko.yandexrestservice.config;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
public class AuditConfiguration {
    @Bean
    AuditReader auditReader(EntityManagerFactory entityManagerFactory) {
        return AuditReaderFactory.get(entityManagerFactory.createEntityManager());
    }
}