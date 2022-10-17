package com.zhmenko.yandexrestservice;

import com.zhmenko.yandexrestservice.util.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

import static com.zhmenko.yandexrestservice.util.JsonUtils.parseJsonArray;
import static com.zhmenko.yandexrestservice.util.JsonUtils.sortJsonArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StatisticsControllerTests extends AbstractTest {
    @Test
    void givenIdAndFromAndToDates_WhenGetStatistic_ThenStatus200AndCorrectData() throws Exception {
        // upload data with same object changing to generate history
        List<String> data = parseJsonArray(resourceDirectory + "/statistic_test/statistic_batch_data.json");

        for (String dataObject : data) {
            mockMvc.perform(
                            post(baseApiUrl + "/imports")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dataObject))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        String dateStart = "2022-02-02T12:00:00.000Z";
        String dateEnd = "2022-02-02T18:00:00.000Z";

        MvcResult mvcResult = mockMvc.perform(
                        get(baseApiUrl +
                                MessageFormat.format("/node/{0}/statistic?dateStart={1}&dateEnd={2}",
                                        rootUUID, dateStart, dateEnd)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONParser jsonParser = new JSONParser();

        JSONObject actualObject = (JSONObject) jsonParser.parse(StringUtils.mapStringBody(mvcResult));
        assertNotNull(actualObject.get("items"));
        assertTrue(actualObject.get("items") instanceof JSONArray);
        JSONArray actualArray = (JSONArray) actualObject.get("items");

        JSONObject expectedObject = (JSONObject) jsonParser.parse(
                Files.newBufferedReader(Path.of(resourceDirectory + "/statistic_test/expected.json")));
        assertNotNull(expectedObject.get("items"));
        assertTrue(expectedObject.get("items") instanceof JSONArray);
        JSONArray expectedArray = (JSONArray) expectedObject.get("items");

        sortJsonArray(actualArray);
        sortJsonArray(expectedArray);

        assertEquals(expectedArray.toJSONString(), actualArray.toJSONString());
    }

    @Test
    void givenIdAndFromAndToDates_WhenGetDeletedShopUnitStatistic_ThenStatus404() throws Exception {
        // Заполняем
        List<String> data = parseJsonArray(resourceDirectory + "/statistic_test/statistic_batch_data.json");
        for (String dataObject : data) {
            mockMvc.perform(
                            post(baseApiUrl + "/imports")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dataObject))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        String dateStart = "2022-02-02T12:00:00.000Z";
        String dateEnd = "2022-02-02T18:00:00.000Z";

        // Проверяем, что 200
        mockMvc.perform(
                        get(baseApiUrl +
                                MessageFormat.format("/node/{0}/statistic?dateStart={1}&dateEnd={2}",
                                        rootUUID, dateStart, dateEnd)))
                .andExpect(status().isOk())
                .andReturn();
        // Удаляем
        mockMvc.perform(
                        delete(baseApiUrl + MessageFormat.format("/delete/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andReturn();
        // Проверяем, что 404
        mockMvc.perform(
                        get(baseApiUrl +
                                MessageFormat.format("/node/{0}/statistic?dateStart={1}&dateEnd={2}",
                                        rootUUID, dateStart, dateEnd)))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void givenIdAndFromAndToDates_WhenGetStatisticWithBadFormatDate_ThenStatus400() throws Exception {
        String dateStart = "2022-02-02 12.00.00";
        String dateEnd = "2022-02-02T18:00:00.000Z";

        mockMvc.perform(
                        get(baseApiUrl +
                                MessageFormat.format("/node/{0}/statistic?dateStart={1}&dateEnd={2}",
                                        rootUUID, dateStart, dateEnd)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Sql("/test_data/init_data_actions/shop_unit.sql")
    void givenIdAndFromAndToDates_WhenGetStatisticWithNullDateParameters_ThenStatus200() throws Exception {
        String[] dateStartArray = {null, "2022-02-02T12:00:00.000Z", null};
        String[] dateEndArray = {null, null, "2022-02-02T18:00:00.000Z"};
        assertEquals(dateStartArray.length, dateEndArray.length);

        for (int i = 0; i < dateStartArray.length; i++) {
            String startDateParameter = dateStartArray[i] == null ? "" : "dateStart=" + dateStartArray[i];
            String endDateParameter = dateEndArray[i] == null ? "" : "dateEnd=" + dateEndArray[i];
            String question = dateStartArray[i] == null && dateEndArray[i] == null ? "" : "?";
            mockMvc.perform(
                            get(baseApiUrl +
                                    MessageFormat.format("/node/{0}/statistic{1}{2}{3}",
                                            rootUUID, question, startDateParameter, endDateParameter)))
                    .andExpect(status().isOk())
                    .andReturn();
        }
    }

    @Test
    void givenIdAndFromAndToDates_WhenGetStatisticWithBadDatesBorders_ThenStatus400() throws Exception {
        String dateStart = "2022-02-02T18:00:00.000Z";
        String dateEnd = "2022-02-02T12:00:00.000Z";

        mockMvc.perform(
                        get(baseApiUrl +
                                MessageFormat.format("/node/{0}/statistic?dateStart={1}&dateEnd={2}",
                                        rootUUID, dateStart, dateEnd)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
