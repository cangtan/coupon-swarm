package org.example.coupon.distribution.dto;

import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.common.constant.CouponStatus;
import org.example.coupon.common.constant.PeriodType;
import org.example.coupon.common.constant.TemplateRule;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponClassify {
    private List<UserCouponDTO> usableList;
    private List<UserCouponDTO> usedList;
    private List<UserCouponDTO> expiredList;

    public static CouponClassify classify(List<UserCouponDTO> coupons) {
        List<UserCouponDTO> usableList = new ArrayList<>(coupons.size());
        List<UserCouponDTO> usedList = new ArrayList<>(coupons.size());
        List<UserCouponDTO> expiredList = new ArrayList<>(coupons.size());
        coupons.forEach(item -> {
            boolean isExpired;
            long curTime = new Date().getTime();
            TemplateRule rule = item.getCouponTemplate().getRule();
            if (rule.getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                isExpired = rule.getExpiration().getDeadLine() <= curTime;
            } else {
                isExpired = DateUtil.offsetDay(item.getAssignTime(), rule.getExpiration().getGap()).getTime() <= curTime;
            }
            if (item.getStatus() == CouponStatus.USRED) {
                usedList.add(item);
            } else if (item.getStatus() == CouponStatus.EXPIRED || isExpired){
                expiredList.add(item);
            } else {
                usableList.add(item);
            }
        });
        return new CouponClassify(usableList, usedList, expiredList);
    }
}
