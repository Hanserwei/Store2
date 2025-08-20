package com.hanserwei.service;

import com.hanserwei.entity.dto.CartItemDTO;
import com.hanserwei.entity.dto.OrderDTO;
import com.hanserwei.entity.po.OrderItem;
import com.hanserwei.entity.vo.OrderVO;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Long createOrder(OrderDTO orderDTO);
    
    List<OrderVO> getOrdersByUserId(Long userId);

    boolean cancelOrder(Long orderId, Long currentUserId);

    boolean payOrder(Long orderId, Long userId, BigDecimal balance);

    List<OrderItem> selectByOrderId(List<Long> orderIdsList);

    boolean directBuy(CartItemDTO cartItemDTO);
}
