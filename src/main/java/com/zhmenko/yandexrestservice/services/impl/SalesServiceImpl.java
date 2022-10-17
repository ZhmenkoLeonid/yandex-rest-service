package com.zhmenko.yandexrestservice.services.impl;

import com.zhmenko.yandexrestservice.mappers.ShopUnitStatisticUnitMapper;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitStatisticResponse;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitStatisticUnit;
import com.zhmenko.yandexrestservice.services.SalesService;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.hibernate.envers.query.AuditEntity.property;
import static org.hibernate.envers.query.AuditEntity.revisionNumber;

@Service
@RequiredArgsConstructor
public class SalesServiceImpl implements SalesService {
    private final AuditReader auditReader;

    private final OffsetDateTime minOffsetDateTime = OffsetDateTime.of(1970,1,1,0,0,0,0, ZoneOffset.UTC);
    private final OffsetDateTime maxOffsetDateTime = OffsetDateTime.of(4000,1,1,0,0,0,0, ZoneOffset.UTC);
    @Override
    public ShopUnitStatisticResponse findUpdatedUnitsBetweenDates(OffsetDateTime fromInclusive, OffsetDateTime toInclusive) {
        if (fromInclusive == null) fromInclusive = minOffsetDateTime;
        if (toInclusive == null) toInclusive = maxOffsetDateTime;

        //revision result always returns Object[4] List
        @SuppressWarnings("unchecked")
        List<Object[]> revisionResult = auditReader.createQuery()
                .forRevisionsOfEntityWithChanges(ShopUnit.class, false)
                .add(property("date").between(fromInclusive, toInclusive))
                .addOrder(revisionNumber().desc())
                .getResultList();

        List<ShopUnitStatisticUnit> historyList = revisionResult.stream()
                .map(arrObj -> (ShopUnit) arrObj[0])
                .map(ShopUnitStatisticUnitMapper::fromShopUnit)
                .collect(Collectors.toList());

        TreeSet<ShopUnitStatisticUnit> distinctByIdSet =
                new TreeSet<>((first,second) -> second.getId().compareTo(first.getId()));
        distinctByIdSet.addAll(historyList);
        return new ShopUnitStatisticResponse(new ArrayList<>(distinctByIdSet));
    }
}
