package com.zhmenko.yandexrestservice.validators.id;

import com.zhmenko.yandexrestservice.data.UnitRepository;
import com.zhmenko.yandexrestservice.model.ShopUnit;
import com.zhmenko.yandexrestservice.model.ShopUnitType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class IdValueValidator implements ConstraintValidator<IdValue,Object> {
    private String shopUnitTypeField;
    private String idField;

    private final UnitRepository unitRepository;

    @Override
    public void initialize(IdValue constraintAnnotation) {
        shopUnitTypeField = constraintAnnotation.shopUnitTypeField();
        idField = constraintAnnotation.idField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        ShopUnitType shopUnitType = (ShopUnitType) new BeanWrapperImpl(value)
                .getPropertyValue(shopUnitTypeField);
        UUID id = (UUID) new BeanWrapperImpl(value)
                .getPropertyValue(idField);

        ShopUnit shopUnit = unitRepository.findById(id).orElse(null);
        return shopUnit == null || shopUnitType == shopUnit.getType();
    }
}
