CREATE DATABASE IF NOT EXISTS coupon_swarm CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE coupon_swarm;
-- ----------------------------
-- Table structure for coupon_template
-- ----------------------------
DROP TABLE IF EXISTS `coupon_template`;
CREATE TABLE `coupon_template`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `available` tinyint NOT NULL COMMENT '是否可用（1:可用 0:不可用）',
  `expired` tinyint NOT NULL COMMENT '是否过期（1:过期 0:未过期）',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券名称',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券logo',
  `intro` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券介绍',
  `category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券分类',
  `product_line` int NOT NULL COMMENT '产品线',
  `coupon_count` int NOT NULL COMMENT '优惠券个数',
  `created_time` datetime(0) NOT NULL COMMENT '创建时间',
  `user_id` bigint NOT NULL COMMENT '创建者id',
  `template_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标识',
  `target` int NOT NULL COMMENT '用途',
  `rule` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券规则',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ux_name`(`name`) USING BTREE,
  INDEX `idx_category`(`category`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `id` int NOT NULL COMMENT 'id',
  `template_id` int NOT NULL COMMENT '优惠券模板id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `coupon_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '优惠券码',
  `assign_time` datetime(0) NOT NULL COMMENT '领取时间',
  `status` int NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_template_id`(`template_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户优惠券' ROW_FORMAT = Dynamic;
