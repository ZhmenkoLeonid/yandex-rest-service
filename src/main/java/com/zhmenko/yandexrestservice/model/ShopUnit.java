package com.zhmenko.yandexrestservice.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.zhmenko.yandexrestservice.serializer.ShopUnitSerializer;
import io.swagger.annotations.ApiModelProperty;

import java.time.OffsetDateTime;
import java.util.*;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * ShopUnit
 */
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "ShopUnit")
@JsonSerialize(using = ShopUnitSerializer.class)
public class ShopUnit {
    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    @Column(name = "date")
    private OffsetDateTime date;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "parentId")
    @ToString.Exclude
    @JsonProperty("parentId")
    private ShopUnit parent;

    @Column(name = "type")
    private ShopUnitType type;

    @Column(name = "price")
    private Long price;

    @Valid
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<ShopUnit> children;

    public ShopUnit(UUID id, String name, OffsetDateTime date, ShopUnitType type, Long price) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.type = type;
        this.price = price;
    }

    /**
     * Уникальный идентфикатор
     *
     * @return id
     */
    @ApiModelProperty(example = "3fa85f64-5717-4562-b3fc-2c963f66a333", required = true, value = "Уникальный идентфикатор")
    @NotNull
    @Valid
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Имя категории
     *
     * @return name
     */
    @ApiModelProperty(required = true, value = "Имя категории")
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Время последнего обновления элемента.
     *
     * @return date
     */
    @ApiModelProperty(example = "2022-05-28T21:12:01Z", required = true, value = "Время последнего обновления элемента.")
    @NotNull
    @Valid
    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }


    @Valid
    public ShopUnit getParent() {
        return parent;
    }

    public void setParent(ShopUnit parent) {
        this.parent = parent;
    }

    /**
     * Get type
     *
     * @return type
     */
    @ApiModelProperty(required = true, value = "")
    @NotNull
    @Valid
    public ShopUnitType getType() {
        return type;
    }

    public void setType(ShopUnitType type) {
        this.type = type;
    }

    /**
     * Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий). Если цена является не целым числом, округляется в меньшую сторону до целого числа. Если категория не содержит товаров цена равна null.
     *
     * @return price
     */
    @ApiModelProperty(value = "Целое число, для категории - это средняя цена всех дочерних товаров(включая товары подкатегорий). Если цена является не целым числом, округляется в меньшую сторону до целого числа. Если категория не содержит товаров цена равна null.")
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * Список всех дочерних товаров\\категорий. Для товаров поле равно null.
     *
     * @return children
     */
    @ApiModelProperty(value = "Список всех дочерних товаров\\категорий. Для товаров поле равно null.")
    @Valid
    public List<ShopUnit> getChildren() {
        return children;
    }

    public void setChildren(List<ShopUnit> children) {
        this.children = children;
    }

    public void addChildren(ShopUnit unit) {
        if (children == null) children = new ArrayList<>();
        unit.setParent(this);
        children.add(unit);
    }

    public List<Long> recalculatePrice() {
        if (type.equals(ShopUnitType.OFFER)) {
            List<Long> prices = new ArrayList<>();
            prices.add(price);
            return prices;
        }
        List<Long> newPrices = new ArrayList<>();
        if (children.size() == 0) {
            price = null;
        } else {
            for (ShopUnit child : children) {
                newPrices.addAll(child.recalculatePrice());
            }
            OptionalDouble avg = newPrices.stream()
                    .mapToLong(e -> e)
                    .average();
            price = avg.isPresent() ? (long) Math.floor(avg.getAsDouble()) : null;
        }
        return newPrices;
    }
}