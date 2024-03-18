package org.example.coupon.distribution.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.example.coupon.bo.TemplateRule;
import org.example.coupon.distribution.dataobject.UserCoupon;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 优惠券模板序列化器
 */
public class UserCouponSerialize extends JsonSerializer<UserCoupon> {
    @Override
    public void serialize(UserCoupon userCoupon,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        // 开始序列化对象
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", userCoupon.getId().toString());
        jsonGenerator.writeStringField("templateId", userCoupon.getTemplateId().toString());
        jsonGenerator.writeStringField("userId", userCoupon.getUserId().toString());
        jsonGenerator.writeStringField("couponCode", userCoupon.getCouponCode());
        jsonGenerator.writeStringField("assignTime",
                new SimpleDateFormat("yyyy-MM-dd").format(userCoupon.getAssignTime()));
        jsonGenerator.writeStringField("name", userCoupon.getCouponTemplate().getName());
        jsonGenerator.writeStringField("logo", userCoupon.getCouponTemplate().getLogo());
        jsonGenerator.writeStringField("intro", userCoupon.getCouponTemplate().getIntro());
        TemplateRule rule = userCoupon.getCouponTemplate().getRule();
        ObjectMapper objectMapper = new ObjectMapper();
        jsonGenerator.writeStringField("expiration", objectMapper.writeValueAsString(rule.getExpiration()));
        jsonGenerator.writeStringField("discount", objectMapper.writeValueAsString(rule.getDiscount()));
        jsonGenerator.writeStringField("usage", objectMapper.writeValueAsString(rule.getUsage()));
        // 结束序列化对象
        jsonGenerator.writeEndObject();
    }
}
