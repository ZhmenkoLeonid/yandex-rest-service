package com.zhmenko.yandexrestservice.model;

import java.util.Objects;
import io.swagger.annotations.ApiModel;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Тип элемента - категория или товар
 */
public enum ShopUnitType {
  
  OFFER("OFFER"),
  
  CATEGORY("CATEGORY");

  private String value;

  ShopUnitType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static ShopUnitType fromValue(String value) {
    for (ShopUnitType b : ShopUnitType.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

