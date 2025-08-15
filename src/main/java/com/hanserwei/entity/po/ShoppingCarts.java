package com.hanserwei.entity.po;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 购物车表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCarts implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
    * 购物车唯一标识
    */
    private Long id;

    /**
    * 关联到users表的用户ID
    */
    private Long userId;

    /**
    * 购物车创建时间
    */
    private LocalDateTime createdAt;

    /**
    * 购物车最后修改时间
    */
    private LocalDateTime updatedAt;
}