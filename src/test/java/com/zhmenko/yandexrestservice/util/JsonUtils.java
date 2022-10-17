package com.zhmenko.yandexrestservice.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class JsonUtils {

    public static String parseJsonFromFile(String filePath) throws IOException, ParseException {
        return new JSONParser()
                .parse(Files.newBufferedReader(Path.of(filePath))).toString();
    }
    public static List<String> parseJsonArray(String filePath) throws ParseException, IOException {
        JSONArray arr = (JSONArray) new JSONParser().parse(Files.readString(Path.of(filePath)));
        return (List<String>) arr.stream().map(Object::toString).collect(Collectors.toList());
    }


    /**
     * Sort by property "date" with Date type
     */
    public static boolean deepSortAndCompare(String jsonOne, String jsonTwo, String arrayPropertyName) throws ParseException {
        JSONObject jsonObject1 = (JSONObject) new JSONParser().parse(jsonOne);
        JSONObject jsonObject2 = (JSONObject) new JSONParser().parse(jsonTwo);
        deepSort(jsonObject1, arrayPropertyName);
        deepSort(jsonObject2, arrayPropertyName);
        log.info(jsonObject1.toJSONString());
        log.info(jsonObject2.toJSONString());
        return jsonObject1.toJSONString().equals(jsonObject2.toJSONString());
    }

    /**
     * Sort by property "date" with Date type
     */
    public static void sortJsonArray(JSONArray jsonArray) {
        List<JSONObject> jsonList = new ArrayList<>();
        Iterator<JSONObject> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject next = iterator.next();
            jsonList.add(next);
            iterator.remove();
        }
        jsonList.sort(new DateJsonComparator());

        jsonArray.addAll(jsonList);
    }

    public static void sortJsonArray(JSONArray jsonArray, Comparator<? super JSONObject> comparator) {
        List<JSONObject> jsonList = new ArrayList<>();
        Iterator<JSONObject> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject next = iterator.next();
            jsonList.add(next);
            iterator.remove();
        }
        jsonList.sort(comparator);

        jsonArray.addAll(jsonList);
    }

    /**
     * Sort by property "date" with Date type
     */
    public static void deepSort(JSONObject obj, String arrayPropertyName) {
        if (obj.get(arrayPropertyName) != null) {
            JSONArray arr = (JSONArray) obj.get(arrayPropertyName);

            List<JSONObject> jsonList = new ArrayList<>();
            Iterator<JSONObject> iterator = arr.iterator();
            while (iterator.hasNext()) {
                JSONObject next =  iterator.next();
                jsonList.add(next);
                iterator.remove();
            }

            jsonList.sort(new DateJsonComparator());

            for (JSONObject jsonObject : jsonList) {
                deepSort(jsonObject, arrayPropertyName);
                arr.add(jsonObject);
            }
        }
    }

    public static void deepSort(JSONObject obj, String arrayPropertyName, Comparator<? super JSONObject> comparator) {
        if (obj.getOrDefault(arrayPropertyName,null) != null) {
            JSONArray arr = (JSONArray) obj.get(arrayPropertyName);

            List<JSONObject> jsonList = new ArrayList<>();
            Iterator<JSONObject> iterator = arr.iterator();
            while (iterator.hasNext()) {
                JSONObject next = iterator.next();
                jsonList.add(next);
                iterator.remove();
            }
            jsonList.sort(comparator);

            for (JSONObject jsonObject : jsonList) {
                deepSort(jsonObject, arrayPropertyName);
                arr.add(jsonObject);
            }
        }
    }

    /**
     *
     Сравнивает элементы по полю "date". Если поля равны, то сравнивает по полю "id"
     */
    static class DateJsonComparator implements Comparator<JSONObject> {
        @Override
        @SneakyThrows
        public int compare(JSONObject o1, JSONObject o2) {
            String v1 = (String) o1.get("date");
            String v2 = (String) o2.get("date");
            long l1 = OffsetDateTime.parse(v1).toEpochSecond();
            long l2 = OffsetDateTime.parse(v2).toEpochSecond();
            int compare = Long.compare(l1, l2);
            return compare == 0 ? ((String) o1.get("id")).compareTo((String) o2.get("id")) : compare;
        }
    }
}
