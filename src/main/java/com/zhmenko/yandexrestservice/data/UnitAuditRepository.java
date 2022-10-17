package com.zhmenko.yandexrestservice.data;

import com.zhmenko.yandexrestservice.model.unit_revision.ShopUnitAudEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UnitAuditRepository extends CrudRepository<ShopUnitAudEntity, UUID> {
}
