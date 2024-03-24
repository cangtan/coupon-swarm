package org.example.coupon.distribution.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.coupon.common.dto.CouponTemplateDTO;
import org.example.coupon.common.dto.GoodsInfoDTO;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementInfo {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品列表
     */
    private List<GoodsInfoDTO> goodsInfoList;

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
        private CouponTemplateDTO template;
    }
}