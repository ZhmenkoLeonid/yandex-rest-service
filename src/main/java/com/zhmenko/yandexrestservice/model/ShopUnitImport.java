package com.zhmenko.yandexrestservice.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.zhmenko.yandexrestservice.model.ShopUnitType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.UUID;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.openapitools.jackson.nullable.JsonNullable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ShopUnitImport
 */
@EqualsAndHashCode
@ToString
public class ShopUnitImport   {

  private UUID id;

  private String name;

  private UUID parentId;

  private ShopUnitType type;

  @Min(0)
  private Long price;

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
   * Имя элемента.
   * @return name
  */
  @ApiModelProperty(required = true, value = "Имя элемента.")
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
   * Целое число, для категорий поле должно содержать null.
   * @return price
  */
  @ApiModelProperty(value = "Целое число, для категорий поле должно содержать null.")
  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

}

