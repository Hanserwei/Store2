package com.hanserwei.mapper;

import com.hanserwei.entity.po.OrderItem;
import com.hanserwei.entity.po.OrderItemBatch;
import com.hanserwei.entity.po.Orders;
import com.hanserwei.entity.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {
    Long insertOneOrder(Orders orders);

    List<OrderVO> selectByUserId(Long userId);

    int cancelOrder(@Param("orderId") Long orderId,@Param("currentUserId") Long currentUserId);

    int cancelOrderItems(Long id);

    Orders selectById(Long orderId);

    int updateBalance(@Param("userId") Long userId, @Param("balance") BigDecimal balance);

    int updateById(Orders orders);

    List<OrderItem> selectByOrderId(Long orderId);

    int batchUpdateProductStock(@Param("map") Map<Long, Long> productIdAndQuantity);

    OrderItem selectItemByOrderId(Long orderId);

    int insertOneOrderItem(@Param("orderId") Long orderId,
                           @Param("productId") Long productId,
                           @Param("quantity") Integer quantity,
                           @Param("subtotal") BigDecimal subtotal,
                           @Param("createdAt") LocalDateTime createdAt);

    int batchInsertOrderItem(List<OrderItemBatch> orderItemBatches);
}