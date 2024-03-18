package org.example.coupon.template.schedule;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.coupon.bo.TemplateRule;
import org.example.coupon.template.dao.CouponTemplateDao;
import org.example.coupon.template.dataobject.CouponTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScheduledTask {
    private final CouponTemplateDao templateDao;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void offlineCouponTemplate() {
        log.info("Start To Expire CouponTemplate.");
        List<CouponTemplate> templateList = templateDao.findAllByExpired(false);
        if (CollectionUtils.isEmpty(templateList)) {
            log.info("Done To Expire CouponTemplate.");
            return;
        }
        Date cur = new Date();
        List<CouponTemplate> expiredList = new ArrayList<>(templateList.size());
        templateList.forEach(item -> {
            TemplateRule rule = item.getRule();
            if (rule.getExpiration().getDeadLine() < cur.getTime()) {
                item.setExpired(true);
                expiredList.add(item);
            }
        });
        if (!expiredList.isEmpty()) {
            templateDao.saveAll(expiredList);
            log.info("Expired CouponTemplate Num:{}", expiredList.size());
        }
        log.info("Done To Expire CouponTemplate.");
    }
}
