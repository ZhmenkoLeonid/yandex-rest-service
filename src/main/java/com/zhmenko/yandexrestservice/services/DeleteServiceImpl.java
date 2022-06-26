package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import com.zhmenko.yandexrestservice.model.ShopUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeleteServiceImpl implements DeleteService {
    private final UnitRepository unitRepository;

    @Override
    @Transactional
    public boolean deleteById(UUID uuid) {
        if (!unitRepository.existsById(uuid)) return false;
        ShopUnit shopUnit = unitRepository.findById(uuid).get();
        //
        ShopUnit parentUnit = shopUnit.getParent();
        // Пересчитываем среднюю цену родителей с учётом удаляемого элемента(-ов)
        unitRepository.delete(shopUnit);
        if (parentUnit != null) {
            parentUnit.getChildren().remove(shopUnit);
            while (parentUnit.getParent() != null) parentUnit = parentUnit.getParent();
            parentUnit.recalculatePrice();
        }
        return true;
    }
}
