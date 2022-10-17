package com.zhmenko.yandexrestservice.model.unit_revision;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "revision_info")
@Data
public class RevInfoEntity {
    @Id
    @Column(name = "revision_id")
    private Integer revisionId;
    @OneToMany(mappedBy = "revision", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private Set<ShopUnitAudEntity> revisions;
    @Column(name = "rev_timestamp")
    private long revisionTimestamp;
}
