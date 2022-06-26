package com.zhmenko.yandexrestservice.model;

import lombok.Data;


import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class ShopUnitResponse {
    private UUID id;
    private String name;
    private OffsetDateTime date;
    private UUID parentId;
    private ShopUnitType type;
    private Long price;
    private List<ShopUnitResponse> children;
}
