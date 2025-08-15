package com.hanserwei.entity.dto;

import java.math.BigDecimal;

public record OrderDTO(Long userId, String address, BigDecimal totalAmount) {
}
