package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import com.zhmenko.yandexrestservice.model.ShopUnit;
import com.zhmenko.yandexrestservice.model.exceptions.UnitNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class NodesServiceImpl implements NodesService{
    private final UnitRepository unitRepository;
    @Override
    public ShopUnit getNodesById(UUID uuid) {
        return unitRepository.findById(uuid).orElseThrow((Supplier<RuntimeException>) () ->
                new UnitNotFoundException());
    }
}
