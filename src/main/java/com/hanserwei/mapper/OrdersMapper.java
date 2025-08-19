package com.hanserwei.mapper;

import com.hanserwei.entity.po.OrderItem;
import com.hanserwei.entity.po.Orders;
import com.hanserwei.entity.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersMapper {
    Long insertOneOrder(Orders orders);

    List<OrderVO> selectByUserId(Long userId);

    int cancelOrder(Long id);

    int cancelOrderItems(Long id);

    Orders selectById(Long orderId);

    int updateBalance(@Param("userId") Long userId, @Param("balance") BigDecimal balance);

    int updateById(Orders orders);

    List<OrderItem> selectByOrderId(Long orderId);

    int batchUpdateProductStock(@Param("map") Map<Long, Long> productIdAndQuantity);

    OrderItem selectItemByOrderId(Long orderId);
}