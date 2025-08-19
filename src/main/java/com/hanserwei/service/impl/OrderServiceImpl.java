package com.hanserwei.service.impl;

import com.hanserwei.entity.dto.OrderDTO;
import com.hanserwei.entity.po.OrderItem;
import com.hanserwei.entity.po.Orders;
import com.hanserwei.entity.vo.OrderVO;
import com.hanserwei.mapper.Item;
import com.hanserwei.mapper.OrdersMapper;
import com.hanserwei.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class OrderServiceImpl implements OrderService {
    private final OrdersMapper ordersMapper;


    public OrderServiceImpl(OrdersMapper ordersMapper) {
        this.ordersMapper = ordersMapper;
    }

    @Override
    public Long createOrder(OrderDTO orderDTO) {
        Long userId = orderDTO.userId();
        if (userId == null || userId <= 0) {
            throw new RuntimeException("参数错误！");
        }

        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setAddress(orderDTO.address());
        orders.setTotalAmount(orderDTO.totalAmount());
        orders.setStatus(0);
        orders.setCreatedAt(LocalDateTime.now());
        orders.setUpdatedAt(LocalDateTime.now());

        return ordersMapper.insertOneOrder(orders);
    }

    @Override
    public List<OrderVO> getOrdersByUserId(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        return ordersMapper.selectByUserId(userId);
    }

    @Override
    public boolean cancelOrder(Long id) {
        if (id == null) {
            throw new RuntimeException("订单ID不能为空");
        }
        boolean order_result = ordersMapper.cancelOrder(id) > 0;
        if (order_result) {
            // 更新order-items
            return ordersMapper.cancelOrderItems(id) > 0;
        }
        return false;
    }

    @Override
    public boolean payOrder(Long orderId, Long userId, BigDecimal balance) {
        if (orderId == null || userId == null || orderId <= 0 || userId <= 0) {
            throw new RuntimeException("参数错误！");
        }
        // 查询订单
        Orders orders = ordersMapper.selectById(orderId);
        if (orders == null || !Objects.equals(orders.getUserId(), userId)) {
            throw new RuntimeException("订单不存在！");
        }
        if (orders.getTotalAmount().compareTo(balance) > 0) {
            throw new RuntimeException("余额不足！");
        }
        // 划扣余额
        balance = balance.subtract(orders.getTotalAmount());
        boolean balance_result = ordersMapper.updateBalance(userId, balance) > 0;
        if (!balance_result) {
            throw new RuntimeException("支付失败！");
        }
        // 更新订单状态
        orders.setStatus(1);
        orders.setUpdatedAt(LocalDateTime.now());
        orders.setId(orderId);
        boolean order_result = ordersMapper.updateById(orders) > 0;
        if (!order_result) {
            throw new RuntimeException("更新订单失败！");
        }
        // 获取订单项
        Map<Long, Long> productIdAndQuantity = new HashMap<>();
        List<OrderItem> orderItems = ordersMapper.selectByOrderId(orderId);
        for (OrderItem item : orderItems) {
            for (Item itemItem : item.getItems()) {
                Long productId = itemItem.getProductId();
                Long quantity = itemItem.getQuantity();
                productIdAndQuantity.put(productId, productIdAndQuantity.getOrDefault(productId, 0L) + quantity);
            }
        }
        // 更新库存
        int update_result = ordersMapper.batchUpdateProductStock(productIdAndQuantity);
        if (update_result != productIdAndQuantity.size()) {
            throw new RuntimeException("更新库存失败！");
        }
        return true;
    }

    @Override
    public List<OrderItem> selectByOrderId(List<Long> orderIdsList) {
        List<OrderItem> result = new ArrayList<>();
        orderIdsList.forEach(orderId -> {
            if (orderId == null || orderId <= 0) {
                throw new RuntimeException("参数错误！");
            }
            OrderItem orderItems = ordersMapper.selectItemByOrderId(orderId);
            result.add(orderItems);
        });
        return result;
    }
}
