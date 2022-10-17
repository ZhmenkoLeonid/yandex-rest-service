package com.zhmenko.yandexrestservice.services.impl;

import com.zhmenko.yandexrestservice.mappers.ShopUnitStatisticUnitMapper;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitStatisticResponse;
import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnitStatisticUnit;
import com.zhmenko.yandexrestservice.services.NodeStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class NodeStatisticServiceImpl implements NodeStatisticService {
    private final AuditReader auditReader;
    private final OffsetDateTime minOffsetDateTime = OffsetDateTime.of(1970,1,1,0,0,0,0, ZoneOffset.UTC);
    private final OffsetDateTime maxOffsetDateTime = OffsetDateTime.of(4000,1,1,0,0,0,0, ZoneOffset.UTC);

    @Override
    public ShopUnitStatisticResponse findStatisticsById(UUID id, OffsetDateTime fromInclusive, OffsetDateTime toExclusive) {
        if (fromInclusive == null) fromInclusive = minOffsetDateTime;
        if (toExclusive == null) toExclusive = maxOffsetDateTime;
        // Для исключения правой границы (гарантируется, что время кратно секундам)
        else toExclusive = toExclusive.minusSeconds(1);

        //revision result always returns Object[4] List
        @SuppressWarnings("unchecked")
        List<Object[]> revisionResult = auditReader.createQuery()
                .forRevisionsOfEntityWithChanges(ShopUnit.class, false)
                .add(AuditEntity.id().eq(id))
                .add(AuditEntity.property("date").between(fromInclusive, toExclusive))
                .getResultList();

        List<ShopUnitStatisticUnit> historyList = revisionResult.stream()
                .map(arrObj -> (ShopUnit) arrObj[0])
                .map(ShopUnitStatisticUnitMapper::fromShopUnit)
                .collect(Collectors.toList());

        log.info(historyList.toString());
        return new ShopUnitStatisticResponse(historyList);
    }
}
