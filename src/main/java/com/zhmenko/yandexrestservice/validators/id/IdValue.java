package com.zhmenko.yandexrestservice.validators.id;

import com.zhmenko.yandexrestservice.validators.price.PriceValueValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = IdValueValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface IdValue {
    String message() default "Изменение типа элемента с товара на категорию или наоборот не допускается";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String shopUnitTypeField() default "type";

    String idField() default "id";
}
