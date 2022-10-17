package com.zhmenko.yandexrestservice.model.unit_revision;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
public class ShopUnitAudEntityPK implements Serializable {
    @Column(name = "revision_id", nullable = false, updatable = false)
    private Integer revisionId;
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private UUID id;
}
