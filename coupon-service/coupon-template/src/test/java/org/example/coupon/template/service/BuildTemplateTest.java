package org.example.coupon.template.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang.time.DateUtils;
import org.example.coupon.bo.TemplateRule;
import org.example.coupon.constant.CouponCategory;
import org.example.coupon.constant.DistributeTarget;
import org.example.coupon.constant.PeriodType;
import org.example.coupon.constant.ProductLine;
import org.example.coupon.template.cmd.TemplateRequest;
import org.example.coupon.template.dataobject.CouponTemplate;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BuildTemplateTest {

    @Autowired
    private IBuildTemplateService buildTemplateService;

    @Test
    public void testBuildTemplate() throws JsonProcessingException, InterruptedException {
        CouponTemplate template = buildTemplateService.buildTemplate(fakeTemplateRequest());
        System.out.println(template);
        Thread.sleep(5000);
    }

    private TemplateRequest fakeTemplateRequest() throws JsonProcessingException {
        TemplateRequest templateRequest = new TemplateRequest();
        templateRequest.setName("优惠券模板名");
        templateRequest.setLogo("https://cangtan.com");
        templateRequest.setIntro("这是一张优惠券");
        templateRequest.setCategory(CouponCategory.MANJIAN.getCode());
        templateRequest.setProductLine(ProductLine.DAMAO.getCode());
        templateRequest.setCouponCount(100);
        templateRequest.setUserId(123L);
        templateRequest.setTarget(DistributeTarget.SINGLE.getCode());
        TemplateRule templateRule = new TemplateRule();
        templateRule.setExpiration(new TemplateRule.Expiration(
                PeriodType.SHIFT.getCode(), 1, DateUtils.addDays(new Date(), 60).getTime()
        ));
        templateRule.setDiscount(new TemplateRule.Discount(5, 1));
        templateRule.setLimitation(1);
        templateRule.setUsage(new TemplateRule.Usage(
                "浙江省", "杭州市",
                new ObjectMapper().writeValueAsString(Arrays.asList("文娱", "家具"))
        ));
        templateRule.setWeight(new ObjectMapper().writeValueAsString(Collections.emptyList()));

        templateRequest.setRule(templateRule);
        return templateRequest;
    }
}
