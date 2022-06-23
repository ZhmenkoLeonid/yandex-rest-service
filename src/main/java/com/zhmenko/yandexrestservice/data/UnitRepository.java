package com.zhmenko.yandexrestservice.data;

import com.zhmenko.yandexrestservice.model.ShopUnit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UnitRepository extends CrudRepository<ShopUnit, UUID> {
}
