package com.zhmenko.yandexrestservice;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;

import static com.zhmenko.yandexrestservice.util.JsonUtils.deepSortAndCompare;
import static com.zhmenko.yandexrestservice.util.StringUtils.mapStringBody;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class NodeControllerTests extends AbstractTest {
    @Test
    @Sql("/test_data/init_data_actions/shop_unit.sql")
    void givenShopUnit_whenGetUnit_ThenStatus200AndShouldReturnCorrectJSONResult() throws Exception {
        String expected = Files.readString(Path.of(resourceDirectory + "/nodes_test/data/expected.json"));
        MvcResult mvcResult = mockMvc.perform(
                        get(baseApiUrl + MessageFormat.format("/nodes/{0}", rootUUID)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertTrue(deepSortAndCompare(expected, mapStringBody(mvcResult), arrayPropertyName));
    }

}
