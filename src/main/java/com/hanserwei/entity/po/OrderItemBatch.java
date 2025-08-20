package com.hanserwei.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemBatch {
    private Long orderId;
    private Long productId;
    private Long quantity;
    private BigDecimal subtotal;
}
