package com.hanserwei.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 购物车商品表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItems implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 购物车商品项唯一标识
     */
    private Long id;

    /**
     * 关联到shopping_carts表的购物车ID
     */
    private Long cartId;

    /**
    * 关联到products表的商品ID
    */
    private Long productId;

    /**
    * 商品数量
    */
    private Integer quantity;

    /**
     * 商品添加到购物车的时间
     */
    private LocalDateTime addedAt;

    /**
     * 商品
     */
    private List<Item> items;
}