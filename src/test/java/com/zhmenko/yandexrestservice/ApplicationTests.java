package com.zhmenko.yandexrestservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zhmenko.yandexrestservice.model.*;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitImport;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitImportRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApplicationTests {
    private static final String baseApiUrl = "http://localhost";

    private File initDataFile = new File("init_data.json");
    private static final String rootUUID = "40dcacd9-90a7-4bb7-b5b6-1832062d8145";
    private static final String categoryUUID = "98883e8f-0507-482f-bce2-2fb306cf6483";
    private static final String categoryUUID1 = "63610f21-1421-4079-99ab-2700b847f7e1";
    private static final String offerUUID = "dba28f59-a639-4b4c-b326-f69e93be94d4";
    private static final String offerUUID1 = "da58936d-7554-4e07-bb5e-d09b69f11952";

    private static ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void init() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());


    }
    @Test
    void initData() throws Exception {
        ShopUnitImportRequest request = new ShopUnitImportRequest();
        List<ShopUnitImport> requestItems = new ArrayList<>();
        request.setUpdateDate(OffsetDateTime.of(2022, 6, 10, 10, 50, 20, 0, ZoneOffset.UTC));

        ShopUnitImport shopUnitImport = new ShopUnitImport();
        shopUnitImport.setId(UUID.fromString(rootUUID));
        shopUnitImport.setName("Товары");
        shopUnitImport.setType(ShopUnitType.CATEGORY);

        requestItems.add(shopUnitImport);
        request.setItems(requestItems);

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Вставка или обновление прошли успешно.",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();

        ShopUnitImportRequest request1 = new ShopUnitImportRequest();
        List<ShopUnitImport> requestItems1 = new ArrayList<>();
        request1.setUpdateDate(OffsetDateTime.of(2022, 6, 11, 15, 20, 40, 0, ZoneOffset.UTC));

        ShopUnitImport shopUnitImport1 = new ShopUnitImport();
        shopUnitImport1.setId(UUID.fromString(categoryUUID));
        shopUnitImport1.setName("Холодильники");
        shopUnitImport1.setType(ShopUnitType.CATEGORY);
        shopUnitImport1.setParentId(shopUnitImport.getId());

        ShopUnitImport shopUnitImport2 = new ShopUnitImport();
        shopUnitImport2.setId(UUID.randomUUID());
        shopUnitImport2.setName("NordFrost");
        shopUnitImport2.setType(ShopUnitType.OFFER);
        shopUnitImport2.setPrice(32999L);
        shopUnitImport2.setParentId(shopUnitImport1.getId());

        ShopUnitImport shopUnitImport3 = new ShopUnitImport();
        shopUnitImport3.setId(UUID.fromString(offerUUID));
        shopUnitImport3.setName("Атлант");
        shopUnitImport3.setType(ShopUnitType.OFFER);
        shopUnitImport3.setPrice(45998L);
        shopUnitImport3.setParentId(shopUnitImport1.getId());

        ShopUnitImport shopUnitImport4 = new ShopUnitImport();
        shopUnitImport4.setId(UUID.fromString(categoryUUID1));
        shopUnitImport4.setName("Стиральные машины");
        shopUnitImport4.setType(ShopUnitType.CATEGORY);
        shopUnitImport4.setParentId(shopUnitImport.getId());

        ShopUnitImport shopUnitImport5 = new ShopUnitImport();
        shopUnitImport5.setId(UUID.randomUUID());
        shopUnitImport5.setName("ColdPoint");
        shopUnitImport5.setType(ShopUnitType.OFFER);
        shopUnitImport5.setPrice(24999L);
        shopUnitImport5.setParentId(shopUnitImport4.getId());

        ShopUnitImport shopUnitImport6 = new ShopUnitImport();
        shopUnitImport6.setId(UUID.fromString(offerUUID1));
        shopUnitImport6.setName("Candy");
        shopUnitImport6.setType(ShopUnitType.OFFER);
        shopUnitImport6.setPrice(67998L);
        shopUnitImport6.setParentId(shopUnitImport4.getId());

        requestItems1.add(shopUnitImport1);
        requestItems1.add(shopUnitImport2);
        requestItems1.add(shopUnitImport3);
        requestItems1.add(shopUnitImport4);
        requestItems1.add(shopUnitImport5);
        requestItems1.add(shopUnitImport6);
        request1.setItems(requestItems1);

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .content(mapper.writeValueAsString(request1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Вставка или обновление прошли успешно.", result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();

        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(shopUnitImport.getId(), actualShopUnit.getId());
                    assertEquals(shopUnitImport.getName(), actualShopUnit.getName());
                    assertEquals(request1.getUpdateDate(), actualShopUnit.getDate());
                    assertEquals(42998L, actualShopUnit.getPrice());
                    assertEquals(shopUnitImport.getType(), actualShopUnit.getType());
                    assertNull(actualShopUnit.getParentId());

                    List<ShopUnitResponse> children = actualShopUnit.getChildren();
                    assertEquals(2, children.size());

                    ShopUnitResponse shopUnitChildren1 = children.get(0);
                    ShopUnitResponse shopUnitChildren2 = children.get(1);
                    assertEquals(2, shopUnitChildren1.getChildren().size());
                    assertEquals(2, shopUnitChildren2.getChildren().size());
                    assertEquals(shopUnitChildren1.getDate(), request1.getUpdateDate());
                    assertEquals(shopUnitChildren2.getDate(), request1.getUpdateDate());
                })
                .andReturn();
    }

    @Test
    void badDateFormatTest() throws Exception {
        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"CATEGORY\",\n" +
                "                \"name\": \"Телевизоры\",\n" +
                "                \"id\": \"069cb8d7-bbdd-47d3-ad8f-82ef4c269df1\",\n" +
                "                \"parentId\": \"" + rootUUID + "\",\n" +
                "                \"price\": null\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-11-25 18:25:10 +03\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void badNameTest() throws Exception {
        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"CATEGORY\",\n" +
                "                \"name\": null,\n" +
                "                \"id\": \"069cb8d7-bbdd-47d3-ad8f-82ef4c269df1\",\n" +
                "                \"parentId\": \"" + rootUUID + "\",\n" +
                "                \"price\": null\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void badTypeTest() throws Exception {
        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"BADTYPE\",\n" +
                "                \"name\": \"Телевизоры\",\n" +
                "                \"id\": \"069cb8d7-bbdd-47d3-ad8f-82ef4c269df1\",\n" +
                "                \"parentId\": \"" + rootUUID + "\",\n" +
                "                \"price\": null\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void badIdTest() throws Exception {
        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"CATEGORY\",\n" +
                "                \"name\": \"Телевизоры\",\n" +
                "                \"id\": \"1\",\n" +
                "                \"parentId\": \"" + rootUUID + "\",\n" +
                "                \"price\": null\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void duplicateUnitIdTest() throws Exception {
        ShopUnitImportRequest request = new ShopUnitImportRequest();
        List<ShopUnitImport> requestItems = new ArrayList<>();
        request.setUpdateDate(OffsetDateTime.of(2022, 6, 10, 10, 50, 20, 0, ZoneOffset.UTC));

        UUID sharedUUID = UUID.randomUUID();

        ShopUnitImport shopUnit1 = new ShopUnitImport();
        shopUnit1.setId(sharedUUID);
        shopUnit1.setType(ShopUnitType.OFFER);
        shopUnit1.setPrice(722L);
        shopUnit1.setName("Мороженное \"Лакомка\"");

        ShopUnitImport shopUnit2 = new ShopUnitImport();
        shopUnit2.setId(sharedUUID);
        shopUnit2.setType(ShopUnitType.OFFER);
        shopUnit2.setPrice(322L);
        shopUnit2.setName("Пони");

        requestItems.add(shopUnit1);
        requestItems.add(shopUnit2);
        request.setItems(requestItems);

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void notExistingParentIdTest() throws Exception {
        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"CATEGORY\",\n" +
                "                \"name\": \"Телевизоры\",\n" +
                "                \"id\": \"069cb8d7-bbdd-47d3-ad8f-82ef4c269df1\",\n" +
                "                \"parentId\": \"d515e43f-f3f6-4471-bb77-6b455017a2d2\",\n" +
                "                \"price\": null\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void categoryByOfferOverrideTest() throws Exception {
        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"OFFER\",\n" +
                "                \"name\": \"Alpen Silver\",\n" +
                "                \"id\": \"" + categoryUUID + "\",\n" +
                "                \"parentId\": \"" + rootUUID + "\",\n" +
                "                \"price\": 79\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void negativePriceOfferTest() throws Exception {
        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"OFFER\",\n" +
                "                \"name\": \"Alpen Silver\",\n" +
                "                \"id\": \"069cb8d7-bbdd-47d3-ad8f-82ef4c269df1\",\n" +
                "                \"parentId\": \"" + rootUUID + "\",\n" +
                "                \"price\": -79\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void nullOfferPrice() throws Exception {
        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"OFFER\",\n" +
                "                \"name\": \"Alpen Silver\",\n" +
                "                \"id\": \"" + offerUUID + "\",\n" +
                "                \"parentId\": \"" + categoryUUID + "\",\n" +
                "                \"price\": null\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(data))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertEquals("{\"code\":400,\"message\":\"Validation Failed\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void removeParentIdParentPriceChangeTest() throws Exception {
        // Проверка цены до запроса

        // Проверка у корневого элемента
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(42998L, actualShopUnit.getPrice());
                })
                .andReturn();
        // Проверка у родителя
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", categoryUUID1)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(46498, actualShopUnit.getPrice());
                    assertEquals(2, actualShopUnit.getChildren().size());
                })
                .andReturn();

        String data = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"OFFER\",\n" +
                "                \"name\": \"Candy\",\n" +
                "                \"id\":\"" + offerUUID1 + "\",\n" +
                "                \"parentId\": null,\n" +
                "                \"price\": 67998\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .content(data)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Вставка или обновление прошли успешно.",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
        // Проверка у корневого элемента
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(34665L, actualShopUnit.getPrice());
                })
                .andReturn();
        // Проверка у родителя
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", categoryUUID1)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(24999L, actualShopUnit.getPrice());
                    assertEquals(1, actualShopUnit.getChildren().size());
                })
                .andReturn();

        // Возвращаем parentId
        String dataOrigin = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"OFFER\",\n" +
                "                \"name\": \"Candy\",\n" +
                "                \"id\": \"" + offerUUID1 + "\",\n" +
                "                \"parentId\": \"" + categoryUUID1 + "\",\n" +
                "                \"price\": 67998\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .content(dataOrigin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Вставка или обновление прошли успешно.",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();

        // Проверка у корневого элемента
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(42998L, actualShopUnit.getPrice());
                })
                .andReturn();
        // Проверка у родителя
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", categoryUUID1)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(46498L, actualShopUnit.getPrice());
                    assertEquals(2, actualShopUnit.getChildren().size());
                })
                .andReturn();
    }

    // ---------------- DELETE TEST SECTION ----------------------------

    @Test
    void priceRecalculatingAfterChildDeleteTest() throws Exception {
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", categoryUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(39498L, actualShopUnit.getPrice());
                    assertEquals(2, actualShopUnit.getChildren().size());
                })
                .andReturn();

        mockMvc.perform(
                delete(baseApiUrl + MessageFormat.format("/delete/{0}", offerUUID)))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Удаление прошло успешно.",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();

        // Проверка у корневого элемента
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(41998L, actualShopUnit.getPrice());
                })
                .andReturn();
        // Проверка у родителя удалённого элемента
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", categoryUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(32999L, actualShopUnit.getPrice());
                    assertEquals(1, actualShopUnit.getChildren().size());
                })
                .andReturn();

        // Возвращаем ребёнка
        String dataOrigin = "    {\n" +
                "        \"items\": [\n" +
                "            {\n" +
                "                \"type\": \"OFFER\",\n" +
                "                \"name\": \"Атлант\",\n" +
                "                \"id\": \"" + offerUUID + "\",\n" +
                "                \"parentId\": \"" + categoryUUID + "\",\n" +
                "                \"price\": 45998\n" +
                "            }\n" +
                "        ],\n" +
                "        \"updateDate\": \"2022-02-02T12:00:00.000Z\"\n" +
                "    }";

        mockMvc.perform(
                post(baseApiUrl + "/imports")
                        .content(dataOrigin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Вставка или обновление прошли успешно.", result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();

        // Проверка у корневого элемента
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(42998L, actualShopUnit.getPrice());
                })
                .andReturn();

        // Проверка у родителя
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", categoryUUID)))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = mapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(39498L, actualShopUnit.getPrice());
                    assertEquals(2, actualShopUnit.getChildren().size());
                })
                .andReturn();
    }

    @Test
    void notExistingIdDeleteTest() throws Exception {
        mockMvc.perform(
                delete(baseApiUrl + "/delete/d6552d6f-2560-4a3a-aa7f-0a7cb3af72b1"))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("{\"code\":404,\"message\":\"Item not found\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }

    @Test
    void okDeleteTest() throws Exception {
        mockMvc.perform(
                delete(baseApiUrl + MessageFormat.format("/delete/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(result ->
                        assertEquals("Удаление прошло успешно.",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();

        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("{\"code\":404,\"message\":\"Item not found\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();

        // Проверка, что были удалены и потомки
        mockMvc.perform(
                get(baseApiUrl + MessageFormat.format("/nodes/{0}", categoryUUID)))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        assertEquals("{\"code\":404,\"message\":\"Item not found\"}",
                                result.getResponse().getContentAsString(StandardCharsets.UTF_8)))
                .andReturn();
    }
}
