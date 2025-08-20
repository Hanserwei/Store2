package com.hanserwei.service.impl;

import com.hanserwei.entity.dto.CartItemDTO;
import com.hanserwei.entity.dto.OrderDTO;
import com.hanserwei.entity.po.*;
import com.hanserwei.entity.vo.OrderVO;
import com.hanserwei.mapper.AddressesMapper;
import com.hanserwei.mapper.CartItemsMapper;
import com.hanserwei.mapper.OrdersMapper;
import com.hanserwei.mapper.ProductsMapper;
import com.hanserwei.service.OrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {
    private final OrdersMapper ordersMapper;
    private final ProductsMapper productsMapper;
    private final AddressesMapper addressesMapper;
    private final CartItemsMapper cartItemsMapper;


    public OrderServiceImpl(OrdersMapper ordersMapper, ProductsMapper productsMapper, AddressesMapper addressesMapper, CartItemsMapper cartItemsMapper) {
        this.ordersMapper = ordersMapper;
        this.productsMapper = productsMapper;
        this.addressesMapper = addressesMapper;
        this.cartItemsMapper = cartItemsMapper;
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

        // 插入订单表
        ordersMapper.insertOneOrder(orders);
        Long ordersId = orders.getId();

        // 插入订单商品表
        List<CartItems> cartItems = cartItemsMapper.selectProductIdAndNameAndQuantity(userId);
        CartItems cartItems1 = cartItems.getFirst();
        List<Item> items = cartItems1.getItems();
        Map<Long, Long> productIdAndQuantity = items.stream().collect(Collectors.toMap(Item::getProductId, Item::getQuantity));
        // 计算小计
        List<OrderItemBatch> orderItemBatches = new ArrayList<>();
        for (Item item : items) {
            Long productId = item.getProductId();
            Long quantity = item.getQuantity();
            BigDecimal price = item.getPrice();
            BigDecimal subtotal = price.multiply(new BigDecimal(quantity));
            OrderItemBatch orderItemBatch = new OrderItemBatch(ordersId, productId, quantity, subtotal);
            orderItemBatches.add(orderItemBatch);
        }
        int insert_result = ordersMapper.batchInsertOrderItem(orderItemBatches);
        if (insert_result != productIdAndQuantity.size()) {
            throw new RuntimeException("插入订单商品表失败！");
        }
        return ordersId;
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

    @Override
    public boolean directBuy(CartItemDTO cartItemDTO) {
        Long userId = cartItemDTO.userId();
        Long productId = cartItemDTO.productId();
        Integer quantity = cartItemDTO.quantity();
        // 参数校验
        if (userId <= 0 || productId <= 0 || quantity <= 0) {
            throw new RuntimeException("参数错误！");
        }
        // 查询库存
        int stock = productsMapper.selectById(productId).getStock();
        if (stock < quantity) {
            throw new RuntimeException("库存不足！");
        }
        // 创建订单
        // 查询默认地址
        String address = addressesMapper.selectDefaultAddress(userId);
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setAddress(address);
        BigDecimal price = productsMapper.selectById(productId).getPrice();
        BigDecimal totalAmount = price.multiply(new BigDecimal(quantity));
        orders.setTotalAmount(totalAmount);
        orders.setStatus(0);
        orders.setCreatedAt(LocalDateTime.now());
        orders.setUpdatedAt(LocalDateTime.now());
        // 插入订单表
        ordersMapper.insertOneOrder(orders);
        // 插入订单项表
        Long orderId = orders.getId();
        BigDecimal subtotal = productsMapper.selectById(productId).getPrice().multiply(new BigDecimal(quantity));
        LocalDateTime createdAt = LocalDateTime.now();
        int result = ordersMapper.insertOneOrderItem(orderId, productId, quantity, subtotal, createdAt);
        if (result <= 0) {
            throw new RuntimeException("创建订单失败！");
        }
        return orderId > 0;
    }
}
