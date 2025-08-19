package com.hanserwei.mapper;

import com.hanserwei.entity.po.OrderItem;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class OrdersMapperTest {

    @Test
    void selectByOrderId() {
        // 创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        // 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        OrdersMapper ordersMapper = sqlSession.getMapper(OrdersMapper.class);
        List<OrderItem> orderItems = ordersMapper.selectByOrderId(2L);
        System.out.println(orderItems);
        // for (OrderItem item : orderItems) {
        //     for (Item itemItem : item.getItems()) {
        //         System.out.println(itemItem.getProductId());
        //         System.out.println(itemItem.getQuantity());
        //     }
        // }
        Map<Long, Long> productIdAndQuantity = new HashMap<>();
        // List<OrderItem> orderItems = ordersMapper.selectByOrderId(orderId);
        for (OrderItem item : orderItems) {
            for (Item itemItem : item.getItems()) {
                Long productId = itemItem.getProductId();
                Long quantity = itemItem.getQuantity();
                productIdAndQuantity.put(productId, productIdAndQuantity.getOrDefault(productId, 0L) + quantity);
            }
        }
        System.out.println(productIdAndQuantity);
    }

    @Test
    void selectItemByOrderId (){
        // 创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        // 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        OrdersMapper ordersMapper = sqlSession.getMapper(OrdersMapper.class);
        OrderItem orderItems = ordersMapper.selectItemByOrderId(2L);
        System.out.println(orderItems);
    }
}