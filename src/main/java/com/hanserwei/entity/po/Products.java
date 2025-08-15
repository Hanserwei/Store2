package com.hanserwei.entity.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 商品表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品唯一标识
     */
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品库存
     */
    private Integer stock;

    /**
     * 关联到categories表的分类ID
     */
    private Long categoryId;

    /**
     * 是否上架，0否 1是
     */
    private Boolean isOnSale;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 信息更新时间
     */
    private LocalDateTime updatedAt;
}