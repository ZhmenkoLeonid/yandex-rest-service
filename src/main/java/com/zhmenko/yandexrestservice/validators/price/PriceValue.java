package com.zhmenko.yandexrestservice.validators.price;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PriceValueValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface PriceValue {
    String message() default "У категорий поле price должно быть null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String shopUnitTypeField() default "type";

    String priceField() default "price";
}
