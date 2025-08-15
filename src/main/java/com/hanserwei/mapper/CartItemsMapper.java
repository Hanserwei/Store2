package com.hanserwei.mapper;

import com.hanserwei.entity.po.CartItems;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartItemsMapper {
    
    CartItems selectByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
    
    List<CartItems> selectByCartId(Long cartId);
    
    int insert(CartItems cartItem);
    
    int updateQuantity(CartItems cartItem);
    
    int deleteByCartIdAndProductId(@Param("cartId") Long cartId, @Param("productId") Long productId);
    
    int deleteByCartId(Long cartId);
}