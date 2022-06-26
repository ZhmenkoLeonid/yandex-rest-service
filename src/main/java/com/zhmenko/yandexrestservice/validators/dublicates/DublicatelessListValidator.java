package com.zhmenko.yandexrestservice.validators.dublicates;

import com.zhmenko.yandexrestservice.model.ShopUnitImport;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

// Проверка на наличие дубликатов в списке по указанному имени поля
public class DublicatelessListValidator implements ConstraintValidator<DublicatelessList, List<ShopUnitImport>> {
    private String field;

    @Override
    public void initialize(DublicatelessList constraintAnnotation) {
        this.field = constraintAnnotation.field();
    }

    @Override
    public boolean isValid(List<ShopUnitImport> list, ConstraintValidatorContext constraintValidatorContext) {
        return list.stream()
                .map(o -> new BeanWrapperImpl(o).getPropertyValue(field).toString())
                .collect(Collectors.groupingBy(Function.identity()))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() > 1)
                .map(Map.Entry::getKey)
                .count() == 0;
    }
}
