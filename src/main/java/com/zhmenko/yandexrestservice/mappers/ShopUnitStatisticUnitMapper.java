package com.zhmenko.yandexrestservice.mappers;

import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitStatisticUnit;

public class ShopUnitStatisticUnitMapper {
    public static ShopUnitStatisticUnit fromShopUnit(ShopUnit shopUnit) {
        ShopUnitStatisticUnit shopUnitStatisticUnit = new ShopUnitStatisticUnit();
        shopUnitStatisticUnit.setId(shopUnit.getId());
        shopUnitStatisticUnit.setName(shopUnit.getName());
        shopUnitStatisticUnit.setPrice(shopUnit.getPrice());
        shopUnitStatisticUnit.setDate(shopUnit.getDate());
        shopUnitStatisticUnit.setType(shopUnit.getType());

        ShopUnit parent = shopUnit.getParent();
        if (parent != null) {
            shopUnitStatisticUnit.setParentId(parent.getId());
        }

        return shopUnitStatisticUnit;
    }
}
