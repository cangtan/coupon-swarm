package org.example.coupon.common.constant;

/**
 * 通用常量定义
 */
public class Constants {
    public static final String TOPIC = "user_coupon_op";

    public static class RedisPrefix {

        public static final String COUPON_TEMPLATE = "coupon_template_code_";

        /**
         *
         */
        public static final String USER_COUPON_USABLE = "user_coupon_usable";

        /**
         * 已使用的
         */
        public static final String USER_COUPON_USED = "user_coupon_used";

        /**
         * 过期的优惠券前缀
         */
        public static final String USER_COUPON_EXIPIRED = "user_coupon_expired";
    }
}
