package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;

import java.util.UUID;

public interface NodesService {
    ShopUnit getNodesById(UUID uuid);
}
