package com.zhmenko.yandexrestservice.model.unit_revision;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Table(name = "shop_unit_audit")
@Data
public class ShopUnitAudEntity {
    @EmbeddedId
    private ShopUnitAudEntityPK id;
    @ManyToOne
    @JoinColumn(name="revision_id", insertable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private RevInfoEntity revision;
    @Column(name = "revision_type")
    private short revisionType;
    @Column(name = "date")
    private Timestamp date;
    @Column(name = "date_mod")
    private Boolean dateMod;
    @Column(name = "name")
    private String name;
    @Column(name = "name_mod")
    private Boolean nameMod;
    @Column(name = "price")
    private Long price;
    @Column(name = "price_mod")
    private Boolean priceMod;
    @Column(name = "type")
    private Integer type;
    @Column(name = "type_mod")
    private Boolean typeMod;
    @Column(name = "parent_id")
    private UUID parentId;
    @Column(name = "parent_mod")
    private Boolean parentMod;
}
