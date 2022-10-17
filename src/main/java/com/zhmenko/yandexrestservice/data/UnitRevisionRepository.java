package com.zhmenko.yandexrestservice.data;

import com.zhmenko.yandexrestservice.model.unit_revision.RevInfoEntity;
import org.springframework.data.repository.CrudRepository;

public interface UnitRevisionRepository extends CrudRepository<RevInfoEntity, Long> {
}
