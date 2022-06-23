package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeleteServiceImpl implements DeleteService {
    private final UnitRepository unitRepository;

    @Override
    public boolean deleteById(UUID uuid) {
        if (!unitRepository.existsById(uuid)) return false;
        unitRepository.deleteById(uuid);
        return true;
    }
}
