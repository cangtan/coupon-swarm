package org.example.coupon.template.converter;

import org.example.coupon.constant.PeriodType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class PeriodTypeConverter implements AttributeConverter<PeriodType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(PeriodType periodType) {
        return periodType.getCode();
    }

    @Override
    public PeriodType convertToEntityAttribute(Integer integer) {
        return PeriodType.of(integer);
    }
}
