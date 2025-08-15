package com.hanserwei.mapper;

import com.hanserwei.entity.po.Orders;
import com.hanserwei.entity.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrdersMapper {
    Long insertOneOrder(Orders orders);
    
    List<OrderVO> selectByUserId(Long userId);
}