package com.hanserwei.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVO {
    private Long productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalPrice;
}