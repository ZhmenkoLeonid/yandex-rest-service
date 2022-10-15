package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitImportRequest;

public interface ImportService {
    void importItems(ShopUnitImportRequest shopUnitImportRequest);
}
