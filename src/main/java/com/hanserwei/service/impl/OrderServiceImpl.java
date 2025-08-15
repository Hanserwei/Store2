package com.hanserwei.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import com.hanserwei.entity.dto.OrderDTO;
import com.hanserwei.entity.po.Orders;
import com.hanserwei.entity.vo.OrderVO;
import com.hanserwei.mapper.AddressesMapper;
import com.hanserwei.mapper.CartItemsMapper;
import com.hanserwei.mapper.OrdersMapper;
import com.hanserwei.mapper.ProductsMapper;
import com.hanserwei.mapper.ShoppingCartsMapper;
import com.hanserwei.service.OrderService;

import java.math.BigDecimal;

public class OrderServiceImpl implements OrderService {
    private final OrdersMapper ordersMapper;
    private final CartItemsMapper cartItemsMapper;
    private final ProductsMapper productsMapper;
    private final ShoppingCartsMapper shoppingCartsMapper;

    public OrderServiceImpl(OrdersMapper ordersMapper, CartItemsMapper cartItemsMapper, 
                           ProductsMapper productsMapper, ShoppingCartsMapper shoppingCartsMapper) {
        this.ordersMapper = ordersMapper;
        this.cartItemsMapper = cartItemsMapper;
        this.productsMapper = productsMapper;
        this.shoppingCartsMapper = shoppingCartsMapper;
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
}
