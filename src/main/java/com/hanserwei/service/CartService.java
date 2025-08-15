package com.hanserwei.service;

import com.hanserwei.entity.dto.CartItemDTO;
import com.hanserwei.entity.vo.CartVO;

import java.util.List;

public interface CartService {
    
    boolean addToCart(CartItemDTO cartItemDTO);
    
    boolean removeFromCart(Long userId, Long productId);
    
    boolean updateCartItemQuantity(Long userId, Long productId, Integer quantity);
    
    List<CartVO> getCartItems(Long userId);
    
    boolean clearCart(Long userId);
    
    CartVO checkout(Long userId);
}