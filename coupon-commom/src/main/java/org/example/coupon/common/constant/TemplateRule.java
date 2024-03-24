package org.example.coupon.common.constant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemplateRule {
    /**
     * 优惠券过期规则
     */
    private Expiration expiration;
    /**
     * 折扣
     */
    private Discount discount;
    /**
     * 每个人最多领几张的限制
     */
    private Integer limitation;
    /**
     * 使用范围
     */
    private Usage usage;

    /**
     * 权重（可以和哪些优惠券一起使用，同一类的优惠券不能一起使用）
     */
    private String weight;

    /**
     * 有效期判断
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Expiration {
        /**
         * 有效期规则， 对应PeriodType的code
         */
        private Integer period;
        /**
         * 有效间隔（只对变动有效期有效
         */
        private Integer gap;
        /**
         * 优惠券模板的失效日期
         */
        private Long deadLine;

        boolean validate() {
            // 简易校验
            return null != PeriodType.of(period) && gap > 0 && deadLine > 0;
        }
    }

    /**
     * 折扣力度
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Discount {
        /**
         * 额度： 满减（20）、折扣（85）、立减（10）
         */
        private Integer quota;

        /**
         * 基准 需要满多少才能使用
         */
        private Integer base;

        boolean validate() {
            return base > 0 && quota > 0;
        }
    }

    /**
     * 使用判断
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Usage {

        /**
         * 省份
         */
        private String province;

        /**
         * 城市
         */
        private String city;

        /**
         * 商品类型
         */
        private String goodsType;
    }
}
