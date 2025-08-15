package com.hanserwei.service;

import com.hanserwei.entity.dto.OrderDTO;
import com.hanserwei.entity.vo.OrderVO;

import java.util.List;

public interface OrderService {
    Long createOrder(OrderDTO orderDTO);
    
    List<OrderVO> getOrdersByUserId(Long userId);
}
