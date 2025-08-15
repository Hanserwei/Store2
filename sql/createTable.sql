-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一标识',
    `username`      VARCHAR(50)     NOT NULL COMMENT '用户名',
    `password_hash` VARCHAR(255)    NOT NULL COMMENT '加密后的密码',
    `email`         VARCHAR(100)    NOT NULL COMMENT '用户邮箱',
    `phone_number`  VARCHAR(20)     NOT NULL COMMENT '用户手机号',
    `created_at`    DATETIME        NOT NULL COMMENT '注册时间',
    `updated_at`    DATETIME        NOT NULL COMMENT '信息更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_username` (`username`),
    UNIQUE KEY `idx_email` (`email`),
    UNIQUE KEY `idx_phone_number` (`phone_number`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- ----------------------------
-- Table structure for addresses
-- ----------------------------
DROP TABLE IF EXISTS `addresses`;
CREATE TABLE `addresses`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '地址唯一标识',
    `user_id`      BIGINT UNSIGNED NOT NULL COMMENT '关联到users表的用户ID',
    `consignee`    VARCHAR(50)     NOT NULL COMMENT '收货人姓名',
    `phone_number` VARCHAR(20)     NOT NULL COMMENT '收货人电话',
    `province`     VARCHAR(50)     NOT NULL COMMENT '省份',
    `city`         VARCHAR(50)     NOT NULL COMMENT '城市',
    `district`     VARCHAR(50)     NOT NULL COMMENT '区/县',
    `street`       VARCHAR(255)    NOT NULL COMMENT '详细街道地址',
    `is_default`   TINYINT(1)      NOT NULL DEFAULT '0' COMMENT '是否为默认地址，0否 1是',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='地址表';

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`
(
    `id`        BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '分类唯一标识',
    `name`      VARCHAR(50)     NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '父分类ID',
    PRIMARY KEY (`id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='商品分类表';

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`
(
    `id`          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品唯一标识',
    `name`        VARCHAR(255)    NOT NULL COMMENT '商品名称',
    `description` TEXT COMMENT '商品描述',
    `price`       DECIMAL(10, 2)  NOT NULL COMMENT '商品价格',
    `stock`       INT UNSIGNED    NOT NULL COMMENT '商品库存',
    `category_id` BIGINT UNSIGNED NOT NULL COMMENT '关联到categories表的分类ID',
    `is_on_sale`  TINYINT(1)      NOT NULL DEFAULT '1' COMMENT '是否上架，0否 1是',
    `created_at`  DATETIME        NOT NULL COMMENT '创建时间',
    `updated_at`  DATETIME        NOT NULL COMMENT '信息更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='商品表';

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`
(
    `id`           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '订单唯一标识',
    `user_id`      BIGINT UNSIGNED NOT NULL COMMENT '关联到users表的用户ID',
    `address`      VARCHAR(256)    NOT NULL COMMENT '订单地址，和地址表关联',
    `total_amount` DECIMAL(10, 2)  NOT NULL COMMENT '订单总金额',
    `status`       TINYINT         NOT NULL COMMENT '订单状态',
    `created_at`   DATETIME        NOT NULL COMMENT '订单创建时间',
    `updated_at`   DATETIME        NOT NULL COMMENT '订单最后修改时间',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_created_at` (`created_at`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单表';

-- ----------------------------
-- Table structure for order_items
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '记录唯一标识',
    `order_id`   BIGINT UNSIGNED NOT NULL COMMENT '关联到orders表的订单ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '关联到products表的商品ID',
    `quantity`   INT UNSIGNED    NOT NULL COMMENT '商品数量',
    `price`      DECIMAL(10, 2)  NOT NULL COMMENT '下单时单个商品的价格',
    `created_at` DATETIME        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='订单商品表';

-- ----------------------------
-- Table structure for shopping_carts
-- ----------------------------
DROP TABLE IF EXISTS `shopping_carts`;
CREATE TABLE `shopping_carts`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '购物车唯一标识',
    `user_id`    BIGINT UNSIGNED NOT NULL COMMENT '关联到users表的用户ID',
    `created_at` DATETIME        NOT NULL COMMENT '购物车创建时间',
    `updated_at` DATETIME        NOT NULL COMMENT '购物车最后修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_user_id` (`user_id`) -- 确保每个用户只有一个购物车
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='购物车表';

-- ----------------------------
-- Table structure for cart_items
-- ----------------------------
DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items`
(
    `id`         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '购物车商品项唯一标识',
    `cart_id`    BIGINT UNSIGNED NOT NULL COMMENT '关联到shopping_carts表的购物车ID',
    `product_id` BIGINT UNSIGNED NOT NULL COMMENT '关联到products表的商品ID',
    `quantity`   INT UNSIGNED    NOT NULL COMMENT '商品数量',
    `added_at`   DATETIME        NOT NULL COMMENT '商品添加到购物车的时间',
    PRIMARY KEY (`id`),
    KEY `idx_cart_id` (`cart_id`),
    KEY `idx_product_id` (`product_id`),
    UNIQUE KEY `idx_cart_product` (`cart_id`, `product_id`) -- 确保同一个商品在同一个购物车中只有一条记录
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='购物车商品表';
