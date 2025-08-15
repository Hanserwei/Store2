package com.hanserwei.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 3809191063908841650L;
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
    private String category;

    /**
     * 是否上架，0否 1是
     */
    private Boolean isOnSale;
}
