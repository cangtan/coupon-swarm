package org.example.coupon.distribution.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.time.DateUtils;
import org.example.coupon.bo.TemplateRule;
import org.example.coupon.constant.PeriodType;
import org.example.coupon.distribution.constant.CouponStatus;
import org.example.coupon.distribution.dataobject.UserCoupon;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponClassify {
    private List<UserCoupon> usableList;
    private List<UserCoupon> usedList;
    private List<UserCoupon> expiredList;

    public static CouponClassify classify(List<UserCoupon> coupons) {
        List<UserCoupon> usableList= new ArrayList<>(coupons.size());
        List<UserCoupon> usedList= new ArrayList<>(coupons.size());
        List<UserCoupon> expiredList= new ArrayList<>(coupons.size());
        coupons.forEach(item -> {
            boolean isExpired;
            long curTime = new Date().getTime();
            TemplateRule rule = item.getCouponTemplate().getRule();
            if (rule.getExpiration().getPeriod().equals(PeriodType.REGULAR.getCode())) {
                isExpired = rule.getExpiration().getDeadLine() <= curTime;
            } else {
                isExpired = DateUtils.addDays(item.getAssignTime(), rule.getExpiration().getGap()).getTime() <= curTime;
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
