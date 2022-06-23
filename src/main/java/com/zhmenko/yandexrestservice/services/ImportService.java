package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.model.ShopUnitImportRequest;

public interface ImportService {
    boolean importItems(ShopUnitImportRequest shopUnitImportRequest);
}
