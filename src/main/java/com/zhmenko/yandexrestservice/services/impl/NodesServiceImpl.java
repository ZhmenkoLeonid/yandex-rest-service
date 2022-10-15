package com.zhmenko.yandexrestservice.services.impl;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;
import com.zhmenko.yandexrestservice.model.exceptions.UnitNotFoundException;
import com.zhmenko.yandexrestservice.services.NodesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class NodesServiceImpl implements NodesService {
    private final UnitRepository unitRepository;
    @Override
    public ShopUnit getNodesById(UUID uuid) {
        return unitRepository.findById(uuid)
                .orElseThrow((Supplier<RuntimeException>) UnitNotFoundException::new);
    }
}
