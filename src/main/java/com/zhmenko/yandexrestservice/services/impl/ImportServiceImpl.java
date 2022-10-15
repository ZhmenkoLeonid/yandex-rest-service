package com.zhmenko.yandexrestservice.services.impl;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitImport;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitImportRequest;
import com.zhmenko.yandexrestservice.model.ShopUnitType;
import com.zhmenko.yandexrestservice.model.exceptions.BadRequestException;
import com.zhmenko.yandexrestservice.services.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.Supplier;

@RequiredArgsConstructor
@Service
public class ImportServiceImpl implements ImportService {
    private final UnitRepository unitRepository;

    @Override
    @Transactional
    public void importItems(ShopUnitImportRequest shopUnitImportRequest) {
        List<ShopUnitImport> items = shopUnitImportRequest.getItems();
        OffsetDateTime updateDate = shopUnitImportRequest.getUpdateDate();

        Map<UUID, ShopUnit> itemMap = new HashMap<>();
        Set<ShopUnit> recalculateSet = new HashSet<>();
        for (ShopUnitImport item : items) {
            ShopUnit shopUnit = unitRepository.findById(item.getId()).orElse(null);
            if (shopUnit == null) {
                shopUnit = new ShopUnit(item.getId(),
                        item.getName(),
                        updateDate,
                        item.getType(),
                        item.getPrice());
            } else {
                shopUnit.setName(item.getName());
                shopUnit.setPrice(item.getPrice());
                shopUnit.setDate(updateDate);
                // Если у unit'a был родитель, а в запросе его уже нет,
                // то нужно пересчитать его стоимость (и родителей родителя) с учётом удаления unit'а,
                if (item.getParentId() == null && shopUnit.getParent() != null ) {
                    ShopUnit tempAncestor = shopUnit.getParent();
                    // Удалить потомка из списка
                    tempAncestor.getChildren().remove(shopUnit);
                    // А также обновить дату изменения родителей
                    tempAncestor.setDate(updateDate);
                    while (tempAncestor.getParent() != null) {
                        tempAncestor = tempAncestor.getParent();
                        tempAncestor.setDate(updateDate);
                    }
                    recalculateSet.add(tempAncestor);
                    shopUnit.setParent(null);
                }
            }
            itemMap.put(item.getId(), shopUnit);
        }

        for (ShopUnitImport item : items) {
            UUID parentId = item.getParentId();
            if (parentId != null) {
                ShopUnit parentShopUnit = itemMap.get(parentId);
                // Если родителя нет во входных данных, ищём в бд
                if (parentShopUnit == null) {
                    parentShopUnit = unitRepository.findById(parentId).orElseThrow((Supplier<RuntimeException>) () ->
                            new BadRequestException("Validation Failed"));
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
                    throw new BadRequestException("Validation Failed");

                parentShopUnit.addChildren(itemMap.get(item.getId()));
            }
        }
        // Пересчитываем цены
        for (ShopUnit shopUnit : recalculateSet) {
            shopUnit.recalculatePrice();
        }
        unitRepository.saveAll(itemMap.values());
    }
}
