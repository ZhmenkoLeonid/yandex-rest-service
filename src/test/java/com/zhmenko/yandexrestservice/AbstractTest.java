package com.zhmenko.yandexrestservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zhmenko.yandexrestservice.data.UnitAuditRepository;
import com.zhmenko.yandexrestservice.data.UnitRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.TimeZone;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AbstractTest {
    protected static final String baseApiUrl = "http://localhost";

    protected static final String resourceDirectory = "src/test/resources/test_data";

    protected static final String arrayPropertyName = "children";

    protected final static UUID rootUUID = UUID.fromString("069cb8d7-bbdd-47d3-ad8f-82ef4c269df1");

    @Autowired
    protected UnitRepository unitRepository;
    @Autowired
    protected UnitAuditRepository unitAuditRepository;
    @Autowired
    protected MockMvc mockMvc;

    protected static ObjectMapper objectMapper;

    @BeforeAll
    public static void init() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @AfterEach
    public void afterTest() {
        unitRepository.deleteAll();
        unitAuditRepository.deleteAll();
    }
}
