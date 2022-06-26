package com.zhmenko.yandexrestservice.validators.price;

import com.zhmenko.yandexrestservice.model.ShopUnitType;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PriceValueValidator implements ConstraintValidator<PriceValue, Object> {
    private String shopUnitTypeField;
    private String priceField;

    @Override
    public void initialize(PriceValue constraintAnnotation) {
        shopUnitTypeField = constraintAnnotation.shopUnitTypeField();
        priceField = constraintAnnotation.priceField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

        ShopUnitType shopUnitType = (ShopUnitType) new BeanWrapperImpl(value)
                .getPropertyValue(shopUnitTypeField);
        Long price = (Long) new BeanWrapperImpl(value)
                .getPropertyValue(priceField);

        if ((shopUnitType == ShopUnitType.CATEGORY && price != null)
                || (shopUnitType == ShopUnitType.OFFER && price == null)) return false;
        return true;
    }
}
