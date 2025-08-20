package com.hanserwei.mapper;

import com.hanserwei.entity.po.CartItems;
import com.hanserwei.entity.po.Item;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

class CartItemsMapperTest {

    @Test
    void selectProductIdAndNameAndQuantity() {
        // 创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        // 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        CartItemsMapper cartItemsMapper = sqlSession.getMapper(CartItemsMapper.class);
        List<CartItems> cartItems = cartItemsMapper.selectProductIdAndNameAndQuantity(1L);
        CartItems cartItem = cartItems.getFirst();
        List<Item> items1 = cartItem.getItems();
        for (Item item : items1) {
            Long productId = item.getProductId();
            String productName = item.getProductName();
            Long quantity = item.getQuantity();
            BigDecimal price = item.getPrice();
            System.out.println("productId: " + productId + ", productName: " + productName + ", quantity: " + quantity + ", price: " + price);
        }
    }
}