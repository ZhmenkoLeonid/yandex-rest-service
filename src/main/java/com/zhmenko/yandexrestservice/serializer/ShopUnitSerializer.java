package com.zhmenko.yandexrestservice.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.zhmenko.yandexrestservice.model.ShopUnit;
import com.zhmenko.yandexrestservice.model.ShopUnitType;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ShopUnitSerializer extends JsonSerializer<ShopUnit> {

    @Override
    public void serialize(ShopUnit value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField("id", value.getId().toString());
        jgen.writeStringField("name", value.getName());
        jgen.writeStringField("date", value.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")));

        if (value.getParent() != null)
            jgen.writeStringField("parentId", value.getParent().getId().toString());
        else jgen.writeNullField("parentId");

        if (value.getPrice() != null) jgen.writeNumberField("price", value.getPrice());
        else jgen.writeNullField("price");

        jgen.writeStringField("type", value.getType().getValue());

        jgen.writeFieldName("children");
        if (value.getChildren().size() == 0 && value.getType().equals(ShopUnitType.OFFER))
            jgen.writeNull();
        else jgen.writeObject(value.getChildren());

        jgen.writeEndObject();
    }

}