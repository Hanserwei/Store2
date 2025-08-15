package com.hanserwei.mapper;

import com.hanserwei.entity.po.ShoppingCarts;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartsMapper {
    
    Long selectByUserId(Long userId);
    
    int insert(ShoppingCarts shoppingCart);
    
    int deleteByUserId(Long userId);
}