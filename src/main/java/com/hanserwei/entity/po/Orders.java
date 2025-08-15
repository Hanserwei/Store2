package com.hanserwei.entity.po;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 订单表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单唯一标识
     */
    private Long id;

    /**
     * 关联到users表的用户ID
     */
    private Long userId;

    /**
     * 订单地址，和地址表关联
     */
    private String address;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    private Integer status;

    /**
     * 订单创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 订单最后修改时间
     */
    private LocalDateTime updatedAt;
}