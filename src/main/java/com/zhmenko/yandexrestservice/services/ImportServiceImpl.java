package com.zhmenko.yandexrestservice.services;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import com.zhmenko.yandexrestservice.model.ShopUnit;
import com.zhmenko.yandexrestservice.model.ShopUnitImport;
import com.zhmenko.yandexrestservice.model.ShopUnitImportRequest;
import com.zhmenko.yandexrestservice.model.ShopUnitType;
import com.zhmenko.yandexrestservice.model.exceptions.BadRequestException;
import com.zhmenko.yandexrestservice.model.exceptions.UnitNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class ImportServiceImpl implements ImportService {
    private final UnitRepository unitRepository;

    @Override
    @Transactional
    public boolean importItems(ShopUnitImportRequest shopUnitImportRequest) {
        List<ShopUnitImport> items = shopUnitImportRequest.getItems();
        OffsetDateTime updateDate = shopUnitImportRequest.getUpdateDate();

        Map<UUID, ShopUnit> itemMap = new HashMap<>();
        Set<ShopUnit> recalculateSet = new HashSet<>();
        for (ShopUnitImport item : items) {
            ShopUnit shopUnit = new ShopUnit(item.getId(),
                    item.getName(),
                    updateDate,
                    item.getType(),
                    item.getPrice());
            itemMap.put(item.getId(), shopUnit);
        }

        for (ShopUnitImport item : items) {
            UUID parentId = item.getParentId();
            if (parentId != null) {
                ShopUnit parentShopUnit = itemMap.get(parentId);
                // Если родителя нет во входных данных, ищём в бд
                if (parentShopUnit == null) {
                    parentShopUnit = unitRepository.findById(parentId).orElseThrow((Supplier<RuntimeException>) () ->
                            new BadRequestException("Невалидная схема документа или входные данные не верны."));
                    // Обновляем дату изменения родителя и родителей родителя
                    parentShopUnit.setDate(updateDate);
                    ShopUnit tempParent = parentShopUnit;
                    while (tempParent.getParent() != null) {
                        tempParent = tempParent.getParent();
                        tempParent.setDate(updateDate);
                    }

                    recalculateSet.add(tempParent);
                    itemMap.put(parentShopUnit.getId(), parentShopUnit);
                } else {
                    // Если родитель во входных данных есть, то далее будем считать его среднюю стоимость,
                    // если это категория и среди входных данных нет других категорий,
                    // которые явл родителем для данной (в случае, если такая имеется, текущая и так посчитается)
                    if (parentShopUnit.getParent() == null
                            && parentShopUnit.getType().equals(ShopUnitType.CATEGORY))
                        recalculateSet.add(parentShopUnit);
                }
                if (parentShopUnit.getType() == ShopUnitType.OFFER)
                    throw new BadRequestException("Невалидная схема документа или входные данные не верны.");

                parentShopUnit.addChildren(itemMap.get(item.getId()));
            }
        }
        // Пересчитываем цены
        for (ShopUnit shopUnit : recalculateSet) {
            shopUnit.recalculatePrice();
        }
        unitRepository.saveAll(itemMap.values());
        return true;
    }
}
