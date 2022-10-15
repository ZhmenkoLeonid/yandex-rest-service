package com.zhmenko.yandexrestservice.data;

import com.zhmenko.yandexrestservice.model.shop_unit.ShopUnit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UnitRepository extends CrudRepository<ShopUnit, UUID> {
    List<ShopUnit> findByDateBetween(OffsetDateTime from, OffsetDateTime to);
}
