package com.zhmenko.yandexrestservice.services.impl;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;
import com.zhmenko.yandexrestservice.services.DeleteService;
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
        if (parentUnit != null) {
            parentUnit.getChildren().remove(shopUnit);
            while (parentUnit.getParent() != null) parentUnit = parentUnit.getParent();
            parentUnit.recalculatePrice();
        } else unitRepository.delete(shopUnit);

/*        // clear history
        clearUnitHistory(shopUnit);*/

        return true;
    }

/*    private void clearUnitHistory(ShopUnit shopUnit) {
        for (ShopUnit child : shopUnit.getChildren()) {
            clearUnitHistory(child);
        }
        AuditQuery query = auditReader.createQuery()
                .forRevisionsOfEntity(ShopUnit.class, false, true);
        query.
    }*/
}
