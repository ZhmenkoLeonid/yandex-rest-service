package com.zhmenko.yandexrestservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zhmenko.yandexrestservice.model.ShopUnit;
import com.zhmenko.yandexrestservice.model.ShopUnitImport;
import com.zhmenko.yandexrestservice.model.ShopUnitImportRequest;
import com.zhmenko.yandexrestservice.model.ShopUnitType;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class YandexRestServiceApplicationTests {
    private final String baseApiUrl = "http://localhost";

    private static ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void init() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void okImportTest() throws Exception {
        ShopUnitImportRequest request = new ShopUnitImportRequest();
        request.setUpdateDate(OffsetDateTime.of(2022, 6, 10, 10, 50, 20, 0, ZoneOffset.UTC));
        List<ShopUnitImport> items = new ArrayList<>();
        ShopUnitImport shopUnitImport = new ShopUnitImport();
        shopUnitImport.setId(UUID.randomUUID());
        shopUnitImport.setName("Холодильники");
        shopUnitImport.setType(ShopUnitType.CATEGORY);

        items.add(shopUnitImport);
        request.setItems(items);

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Вставка или обновление прошли успешно.", result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();

        mockMvc.perform(
                get(baseApiUrl + "/nodes/" + shopUnitImport.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnit actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnit.class);
                    assertEquals(actualShopUnit.getId(), shopUnitImport.getId());
                    assertEquals(actualShopUnit.getName(), shopUnitImport.getName());
                    assertEquals(actualShopUnit.getDate(), request.getUpdateDate());
                    assertEquals(actualShopUnit.getPrice(), shopUnitImport.getPrice());
                    assertEquals(actualShopUnit.getType(), shopUnitImport.getType());
                    assertNull(actualShopUnit.getParent());
                    assertEquals(actualShopUnit.getChildren().size(), 0);
                })
                .andReturn();
    }

}
