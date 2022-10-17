package com.zhmenko.yandexrestservice;

import com.zhmenko.yandexrestservice.util.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.List;

import static com.zhmenko.yandexrestservice.util.JsonUtils.parseJsonArray;
import static com.zhmenko.yandexrestservice.util.JsonUtils.sortJsonArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SalesControllerTests extends AbstractTest {
    @Test
    void givenDate_WhenGetSalesWithCorrectData_ThenStatus200AndCorrectData() throws Exception {
        // upload data to generate history
        List<String> data = parseJsonArray(resourceDirectory + "/sales_test/sales_batch_data.json");

        for (String dataObject : data) {
            mockMvc.perform(
                            post(baseApiUrl + "/imports")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(dataObject))
                    .andExpect(status().isOk())
                    .andReturn();
        }

        // make request, make sure that status 200 and compare with expected
        String date = "2022-02-03T12:00:00.000Z";

        MvcResult mvcResult = mockMvc.perform(
                        get(baseApiUrl +
                                MessageFormat.format("/sales?date={1}",
                                        rootUUID, date)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        JSONParser jsonParser = new JSONParser();

        JSONObject actualObject = (JSONObject) jsonParser.parse(StringUtils.mapStringBody(mvcResult));
        assertNotNull(actualObject.get("items"));
        assertTrue(actualObject.get("items") instanceof JSONArray);
        JSONArray actualArray = (JSONArray) actualObject.get("items");

        JSONObject expectedObject = (JSONObject) jsonParser.parse(
                Files.newBufferedReader(Path.of(resourceDirectory + "/sales_test/expected.json")));
        assertNotNull(expectedObject.get("items"));
        assertTrue(expectedObject.get("items") instanceof JSONArray);
        JSONArray expectedArray = (JSONArray) expectedObject.get("items");

        sortJsonArray(actualArray);
        sortJsonArray(expectedArray);

        assertEquals(expectedArray.toJSONString(), actualArray.toJSONString());
    }

    @Test
    void givenDate_WhenGetSalesWithBadFormatDate_ThenStatus400() throws Exception {
        String date = "2022-02-03 12.00.00.000";

        mockMvc.perform(
                        get(baseApiUrl +
                                MessageFormat.format("/sales?date={1}",
                                        rootUUID, date)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void givenDate_WhenGetSalesWithNullDateParameter_ThenStatus400() throws Exception {
        mockMvc.perform(
                        get(baseApiUrl +
                                MessageFormat.format("/sales",
                                        rootUUID)))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
