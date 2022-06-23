package com.zhmenko.yandexrestservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;
import javax.validation.Valid;

/**
 * ShopUnitStatisticResponse
 */
@EqualsAndHashCode
@ToString
public class ShopUnitStatisticResponse   {
  @Valid
  private List<ShopUnitStatisticUnit> items;

  /**
   * История в произвольном порядке.
   * @return items
  */
  @ApiModelProperty(value = "История в произвольном порядке.")
  @Valid
  public List<ShopUnitStatisticUnit> getItems() {
    return items;
  }

  public void setItems(List<ShopUnitStatisticUnit> items) {
    this.items = items;
  }
}

