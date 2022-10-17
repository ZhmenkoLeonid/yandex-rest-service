package com.zhmenko.yandexrestservice;

import com.zhmenko.yandexrestservice.model.ShopUnitResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

import static com.zhmenko.yandexrestservice.util.JsonUtils.*;
import static com.zhmenko.yandexrestservice.util.JsonUtils.parseJsonFromFile;
import static com.zhmenko.yandexrestservice.util.StringUtils.mapStringBody;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ImportControllerTests extends AbstractTest {
    @Test
    void givenShopUnits_WhenImportShopUnits_ThenStatus200AndShouldCorrectlyCreateOrUpdateShopUnits() throws Exception {
        String[] dirNames = {"1_insert", "2_insert", "3_update"};
        String pathPrefix = resourceDirectory + "/import_test/batches/";
        for (String dirName : dirNames) {
            Path batchDataPath = Path.of(pathPrefix + dirName + "/data.json");
            Path batchExpectedPath = Path.of(pathPrefix + dirName + "/expected.json");
            String request = Files.readString(batchDataPath);
            String expected = Files.readString(batchExpectedPath);
            // Импортируем
            mockMvc.perform(
                            post(baseApiUrl + "/imports")
                                    .content(request)
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(result ->
                            assertEquals("Вставка или обновление прошли успешно.", mapStringBody(result)))
                    .andReturn();
            // Проверяем правильное изменение данных
            MvcResult mvcResult = mockMvc.perform(
                            get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            JSONObject sortedExpected = (JSONObject) new JSONParser().parse(expected);
            deepSort(sortedExpected, "children");
            JSONObject sortedActual = (JSONObject) new JSONParser().parse(mapStringBody(mvcResult));
            deepSort(sortedActual, "children");
            assertEquals(sortedExpected.toString(), sortedActual.toString());
        }
    }

    @Test
    void givenShopUnit_whenImportShopUnitWithBadDateFormat_ThenStatus400() throws Exception {
        List<String> badFormatData = parseJsonArray(resourceDirectory + "/import_test/date/bad_date_format_batch_data.json");
        for (String badFormatDataObject : badFormatData) {
            mockMvc.perform(
                            post(baseApiUrl + "/imports")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(badFormatDataObject))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }
    }

    @Test
    void givenShopUnit_whenImportShopUnitWithBadNameFormat_ThenStatus400() throws Exception {
        String badNameFormatData =
                parseJsonFromFile(resourceDirectory + "/import_test/name/bad_name_format_data.json");
        mockMvc.perform(
                        post(baseApiUrl + "/imports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(badNameFormatData))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void givenShopUnit_whenImportShopUnitWithBadTypeFormat_ThenStatus400() throws Exception {
        List<String> badFormatData = parseJsonArray(resourceDirectory + "/import_test/type/bad_type_format_batch_data.json");
        for (String badFormatDataObject : badFormatData) {
            mockMvc.perform(
                            post(baseApiUrl + "/imports")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(badFormatDataObject))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }
    }

    @Test
    void givenShopUnit_whenImportShopUnitWithBadIdFormat_ThenStatus400() throws Exception {
        List<String> badFormatData = parseJsonArray(resourceDirectory + "/import_test/id/bad_id_format_batch_data.json");
        for (String badFormatDataObject : badFormatData) {
            mockMvc.perform(
                            post(baseApiUrl + "/imports")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(badFormatDataObject))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }
    }

    @Test
    void givenShopUnit_whenImportShopUnitWithBadPriceFormat_ThenStatus400() throws Exception {
        List<String> badFormatData = parseJsonArray(resourceDirectory + "/import_test/price/bad_price_format_batch_data.json");
        for (String badFormatDataObject : badFormatData) {
            mockMvc.perform(
                            post(baseApiUrl + "/imports")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(badFormatDataObject))
                    .andExpect(status().isBadRequest())
                    .andReturn();
        }
    }

    @Test
    void givenShopUnits_whenImportShopUnitsWithSameId_ThenStatus400() throws Exception {
        String badFormatData =
                parseJsonFromFile(resourceDirectory + "/import_test/id/duplicate_id_data.json");
        mockMvc.perform(
                        post(baseApiUrl + "/imports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(badFormatData))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void givenShopUnit_whenImportShopUnitWithNotExistingParentId_ThenStatus400() throws Exception {
        String badFormatData =
                parseJsonFromFile(resourceDirectory + "/import_test/parent_id/using_not_existing_parent_id_data.json");
        mockMvc.perform(
                        post(baseApiUrl + "/imports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(badFormatData))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void givenShopUnits_whenImportShopUnitsAndCategoryBeingOverridingByOffer_ThenStatus400() throws Exception {
        List<String> badFormatData = parseJsonArray(resourceDirectory + "/import_test/type/category_override_by_offer_data.json");

        //import 2 категории без ошибок
        mockMvc.perform(
                        post(baseApiUrl + "/imports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(badFormatData.get(0)))
                .andExpect(status().isOk())
                .andReturn();
        // пытаемся перезаписать одну из них товаром и должны получить статус 400
        mockMvc.perform(
                        post(baseApiUrl + "/imports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(badFormatData.get(1)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Sql("/test_data/init_data_actions/shop_unit.sql")
    void givenShopUnits_whenImportShopUnitThatRemoveChild_ThenParentPriceChange() throws Exception {
        String data = parseJsonFromFile(resourceDirectory + "/import_test/price/parent_price_after_children_change_data.json");

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
        // вносим изменения
        mockMvc.perform(
                        post(baseApiUrl + "/imports")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(data))
                .andExpect(status().isOk())
                .andReturn();

        // проверяем среднюю цену после изменения
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
}
