package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitStatisticResponse;

import java.time.OffsetDateTime;
import java.util.UUID;

public interface NodeStatisticService {
    ShopUnitStatisticResponse findStatisticsById(UUID id, OffsetDateTime fromInclusive, OffsetDateTime toExclusive);
}
