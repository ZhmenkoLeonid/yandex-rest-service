package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitStatisticResponse;

import java.time.OffsetDateTime;

public interface SalesService {
    ShopUnitStatisticResponse findUpdatedUnitsBetweenDates(OffsetDateTime fromInclusive, OffsetDateTime toInclusive);
}
