-- ----------------------------
-- Data for Table categories
-- ----------------------------

-- Top-level categories (parent_id is NULL)
INSERT INTO `categories` (`id`, `name`, `parent_id`)
VALUES (1, '电子产品', NULL),
       (2, '家居生活', NULL),
       (3, '服装配饰', NULL),
       (4, '图书音像', NULL);

-- Sub-categories for '电子产品' (id=1)
INSERT INTO `categories` (`id`, `name`, `parent_id`)
VALUES (101, '手机', 1),
       (102, '笔记本电脑', 1),
       (103, '智能穿戴', 1),
       (104, '平板电脑', 1);

-- Sub-categories for '家居生活' (id=2)
INSERT INTO `categories` (`id`, `name`, `parent_id`)
VALUES (201, '厨房用品', 2),
       (202, '卧室家具', 2),
       (203, '家纺', 2),
       (204, '清洁用品', 2);

-- Sub-categories for '服装配饰' (id=3)
INSERT INTO `categories` (`id`, `name`, `parent_id`)
VALUES (301, '男装', 3),
       (302, '女装', 3),
       (303, '运动鞋', 3),
       (304, '箱包', 3);

-- Sub-categories for '图书音像' (id=4)
INSERT INTO `categories` (`id`, `name`, `parent_id`)
VALUES (401, '小说', 4),
       (402, '历史', 4),
       (403, '音乐CD', 4);


-- ----------------------------
-- Data for Table products
-- ----------------------------

-- Electronics products
INSERT INTO `products` (`id`, `name`, `description`, `price`, `stock`, `category_id`, `is_on_sale`, `created_at`,
                        `updated_at`)
VALUES (1001, 'iPhone 15 Pro Max', 'Apple iPhone 15 Pro Max 256GB', 9999.00, 500, 101, 1, '2023-09-20 10:00:00',
        '2023-09-20 10:00:00'),
       (1002, '华为Mate 60 Pro', '华为Mate 60 Pro 512GB', 6999.00, 300, 101, 1, '2023-08-30 09:30:00',
        '2023-08-30 09:30:00'),
       (1003, 'MacBook Air M2', 'Apple MacBook Air M2芯片 13寸', 8999.00, 200, 102, 1, '2023-07-15 11:00:00',
        '2023-07-15 11:00:00'),
       (1004, '小米手环8 Pro', '小米智能穿戴手环8 Pro', 299.00, 1000, 103, 1, '2023-10-01 14:00:00',
        '2023-10-01 14:00:00');

-- Home and living products
INSERT INTO `products` (`id`, `name`, `description`, `price`, `stock`, `category_id`, `is_on_sale`, `created_at`,
                        `updated_at`)
VALUES (2001, '德国WMF锅具三件套', '高品质不锈钢锅具，适用于各种炉灶', 1299.00, 150, 201, 1, '2023-06-01 10:00:00',
        '2023-06-01 10:00:00'),
       (2002, '北欧简约实木床', '1.8米双人床，现代简约风格', 2500.00, 50, 202, 1, '2023-05-20 16:00:00',
        '2023-05-20 16:00:00'),
       (2003, '纯棉四件套', '亲肤透气纯棉床单被套枕套', 359.00, 300, 203, 1, '2023-07-01 12:00:00',
        '2023-07-01 12:00:00');

-- Apparel and accessories products
INSERT INTO `products` (`id`, `name`, `description`, `price`, `stock`, `category_id`, `is_on_sale`, `created_at`,
                        `updated_at`)
VALUES (3001, '夏季男士短袖T恤', '纯色圆领休闲T恤', 89.00, 800, 301, 1, '2023-04-10 09:00:00', '2023-04-10 09:00:00'),
       (3002, '女士修身牛仔裤', '高腰弹力显瘦小脚裤', 189.00, 600, 302, 1, '2023-04-15 10:00:00',
        '2023-04-15 10:00:00'),
       (3003, 'Nike Air Max运动鞋', '经典气垫跑步鞋', 799.00, 250, 303, 1, '2023-03-20 11:00:00',
        '2023-03-20 11:00:00');

-- Books and audio-visual products
INSERT INTO `products` (`id`, `name`, `description`, `price`, `stock`, `category_id`, `is_on_sale`, `created_at`,
                        `updated_at`)
VALUES (4001, '《三体全集》', '刘慈欣科幻小说巨著', 158.00, 400, 401, 1, '2023-01-01 08:00:00', '2023-01-01 08:00:00'),
       (4002, '《人类简史》', '赫拉利经典历史著作', 68.00, 500, 402, 1, '2023-02-01 09:00:00', '2023-02-01 09:00:00');

-- ----------------------------
-- Data for Table addresses
-- ----------------------------

-- Addresses for user_id = 2
INSERT INTO `addresses` (`user_id`, `consignee`, `phone_number`, `province`, `city`, `district`, `street`, `is_default`)
VALUES (2, '旅行者', '13812345678', '蒙德', '蒙德城', '风神广场', '猎鹿人餐馆', 1),
       (2, '荧', '13812345679', '璃月', '璃月港', '玉京台', '不卜庐旁', 0);

-- Addresses for user_id = 3
INSERT INTO `addresses` (`user_id`, `consignee`, `phone_number`, `province`, `city`, `district`, `street`, `is_default`)
VALUES (3, '空', '13987654321', '稻妻', '稻妻城', '天守阁区', '鸣神大社参道', 1),
       (3, '派蒙', '13987654322', '须弥', '须弥城', '大巴扎', '净善宫附近', 0),
       (3, '达达利亚', '13987654323', '至冬', '至冬国', '债务处理', '壁炉之家', 0);

/* 这个是我测试的时候用AI 生成的SQL，请不要使用，请使用上面的SQL就行
-- ----------------------------
-- Data for Table order_items
-- ----------------------------

-- Assuming order_id = 1 (e.g., a sample order for user_id = 1 or 2)
-- Items for order_id 1: iPhone 15 Pro Max, Mi Band 8 Pro
INSERT INTO `order_items` (`order_id`, `product_id`, `quantity`, `price`, `created_at`)
VALUES (1, 1001, 1, 9999.00, '2024-01-01 10:30:00'), -- iPhone 15 Pro Max
       (1, 1004, 2, 299.00, '2024-01-01 10:30:00');
-- Mi Band 8 Pro

-- Assuming order_id = 2 (e.g., another sample order)
-- Items for order_id 2: WMF Cookware Set, Huawei Mate 60 Pro
INSERT INTO `order_items` (`order_id`, `product_id`, `quantity`, `price`, `created_at`)
VALUES (2, 2001, 1, 1299.00, '2024-01-02 11:00:00'), -- WMF Cookware Set
       (2, 1002, 1, 6999.00, '2024-01-02 11:00:00');
-- Huawei Mate 60 Pro

-- Assuming order_id = 3 (e.g., a third sample order)
-- Items for order_id 3: Pure Cotton Four-Piece Set, The Three-Body Problem
INSERT INTO `order_items` (`order_id`, `product_id`, `quantity`, `price`, `created_at`)
VALUES (3, 2003, 1, 359.00, '2024-01-03 14:00:00'), -- Pure Cotton Four-Piece Set
       (3, 4001, 1, 158.00, '2024-01-03 14:00:00'), -- The Three-Body Problem
       (3, 3003, 1, 799.00, '2024-01-03 14:00:00'); -- Nike Air Max运动鞋
 */


#  2025年08月18日 09:05:05 增加用户表的身份以及余额
alter table users
    add role int default 0 not null comment '用户身份1：admin，0：user';

alter table users
    add balance bigint default 100000 not null comment '余额，用于模拟支付';

