package com.zhmenko.yandexrestservice.model.shop_unit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zhmenko.yandexrestservice.model.ShopUnitType;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ShopUnitStatisticUnit
 */
@EqualsAndHashCode
@ToString
public class ShopUnitStatisticUnit   {
  private UUID id;

  private String name;

  private UUID parentId;

  private ShopUnitType type;

  private Long price;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
  private OffsetDateTime date;

  /**
   * Уникальный идентфикатор
   * @return id
  */
  @ApiModelProperty(example = "3fa85f64-5717-4562-b3fc-2c963f66a333", required = true, value = "Уникальный идентфикатор")
  @NotNull
  @Valid
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Имя элемента
   * @return name
  */
  @ApiModelProperty(required = true, value = "Имя элемента")
  @NotNull
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * UUID родительской категории
   * @return parentId
  */
  @ApiModelProperty(example = "3fa85f64-5717-4562-b3fc-2c963f66a333", value = "UUID родительской категории")
  @Valid
  public UUID getParentId() {
    return parentId;
  }

  public void setParentId(UUID parentId) {
    this.parentId = parentId;
  }

  /**
   * Get type
   * @return type
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull
  @Valid
  public ShopUnitType getType() {
    return type;
  }

  public void setType(ShopUnitType type) {
    this.type = type;
  }

  /**
   * Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий). Если цена является не целым числом, округляется в меньшую сторону до целого числа. Если категория не содержит товаров цена равна null.
   * @return price
  */
  @ApiModelProperty(value = "Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий). Если цена является не целым числом, округляется в меньшую сторону до целого числа. Если категория не содержит товаров цена равна null.")
  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  /**
   * Время последнего обновления элемента.
   * @return date
  */
  @ApiModelProperty(required = true, value = "Время последнего обновления элемента.")
  @NotNull
  @Valid
  public OffsetDateTime getDate() {
    return date;
  }

  public void setDate(OffsetDateTime date) {
    this.date = date;
  }
}

