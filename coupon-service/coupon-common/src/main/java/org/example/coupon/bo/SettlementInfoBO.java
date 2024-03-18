package org.example.coupon.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 结算信息
 * 1.userId
 * 2.商品信息（列表）
 * 3.优惠券列表
 * 4.结算结果金额
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementInfoBO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品列表
     */
    private List<GoodsInfoBO> goodsInfoList;

    /**
     * 用户优惠券信息
     */
    private List<CouponAndTemplateInfo> couponAndTemplateInfoList;

    /**
     * 是否核销
     */
    private Boolean employ;

    /**
     * 结算后花费
     */
    private BigDecimal cost;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CouponAndTemplateInfo {
        /**
         * userCouponId
         */
        private Integer id;
        /**
         * 优惠券对应的模板
         */
        private CouponTemplateBO template;
    }
}
