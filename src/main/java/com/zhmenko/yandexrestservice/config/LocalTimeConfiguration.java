package com.zhmenko.yandexrestservice.config;

import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Configuration
public class LocalTimeConfiguration {
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
