package com.zhmenko.yandexrestservice;

import com.zhmenko.yandexrestservice.model.ShopUnitResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DeleteControllerTests extends AbstractTest {
    @Test
    @Sql("/test_data/init_data_actions/shop_unit.sql")
    void givenShopUnit_whenDeleteShopUnitChild_ThenParentPriceChange() throws Exception {
        // проверяем среднюю цену до изменения
        mockMvc.perform(
                        get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(237, actualShopUnit.getPrice());
                })
                .andReturn();

        // удаляем
        mockMvc.perform(
                        delete(baseApiUrl + MessageFormat.format("/delete/{0}", "280538e0-f5b9-41c6-8dae-225bf8ca0f76")))
                .andExpect(status().isOk())
                .andReturn();

        // проверяем среднюю цену после удаления
        mockMvc.perform(
                        get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    ShopUnitResponse actualShopUnit = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), ShopUnitResponse.class);
                    assertEquals(200, actualShopUnit.getPrice());
                })
                .andReturn();
    }

    @Test
    @Sql("/test_data/init_data_actions/shop_unit.sql")
    void givenShopUnit_whenDeleteShopUnitWithNotExistingId_ThenStatus404() throws Exception {
        mockMvc.perform(
                        delete(baseApiUrl + MessageFormat.format("/delete/{0}", "f1cc8608-2872-4278-b7e7-d71e2400243c")))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Sql("/test_data/init_data_actions/shop_unit.sql")
    void givenShopUnits_whenDeleteCorrectShopUnit_ThenStatus200AndGetDeletedShopUnitAndHisChildrenShouldReturnStatus404() throws Exception {
        mockMvc.perform(
                        delete(baseApiUrl + MessageFormat.format("/delete/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(
                        get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isNotFound())
                .andReturn();

        // Проверка, что были удалены и потомки
        mockMvc.perform(
                        get(baseApiUrl + MessageFormat.format("/nodes/{0}", "a627ad46-2ca6-4e4e-9aeb-0b5a5db7d5ba")))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}