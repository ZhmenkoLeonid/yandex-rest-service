package com.zhmenko.yandexrestservice.model;

import com.zhmenko.yandexrestservice.validators.dublicates.DublicatelessList;
import io.swagger.annotations.ApiModelProperty;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;

/**
 * ShopUnitImportRequest
 */
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ShopUnitImportRequest   {
  @Valid
  @DublicatelessList(field = "id")
  private List<ShopUnitImport> items;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime updateDate;

  /**
   * Импортируемые элементы
   * @return items
  */
  @ApiModelProperty(value = "Импортируемые элементы")
  @Valid
  public List<ShopUnitImport> getItems() {
    return items;
  }

  public void setItems(List<ShopUnitImport> items) {
    this.items = items;
  }

  /**
   * Время обновления добавляемых товаров/категорий.
   * @return updateDate
  */
  @ApiModelProperty(example = "2022-05-28T21:12:01Z", value = "Время обновления добавляемых товаров/категорий.")
  @Valid
  public OffsetDateTime getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(OffsetDateTime updateDate) {
    this.updateDate = updateDate;
  }
}

