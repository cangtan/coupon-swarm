package org.example.coupon.template.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.coupon.common.constant.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class TemplateRuleConverter implements AttributeConverter<TemplateRule, String> {
    @Override
    public String convertToDatabaseColumn(TemplateRule templateRule) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(templateRule);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public TemplateRule convertToEntityAttribute(String s) {
        ObjectMapper objectMapper = new ObjectMapper();

        TemplateRule rule = null;
        try {
            rule = objectMapper.readValue(s, TemplateRule.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        return rule;
    }
}
