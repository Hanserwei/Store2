package com.hanserwei.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.hanserwei.entity.dto.CartItemDTO;
import com.hanserwei.entity.po.CartItems;
import com.hanserwei.entity.po.Products;
import com.hanserwei.entity.po.ShoppingCarts;
import com.hanserwei.entity.vo.CartVO;
import com.hanserwei.mapper.CartItemsMapper;
import com.hanserwei.mapper.ProductsMapper;
import com.hanserwei.mapper.ShoppingCartsMapper;
import com.hanserwei.service.CartService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CartServiceImpl implements CartService {
    
    private final ShoppingCartsMapper shoppingCartsMapper;
    private final CartItemsMapper cartItemsMapper;
    private final ProductsMapper productsMapper;
    
    public CartServiceImpl(ShoppingCartsMapper shoppingCartsMapper, 
                          CartItemsMapper cartItemsMapper, 
                          ProductsMapper productsMapper) {
        this.shoppingCartsMapper = shoppingCartsMapper;
        this.cartItemsMapper = cartItemsMapper;
        this.productsMapper = productsMapper;
    }
    
    @Override
    public boolean addToCart(CartItemDTO cartItemDTO) {
        Long userId = cartItemDTO.userId();
        Long productId = cartItemDTO.productId();
        Integer quantity = cartItemDTO.quantity();
        
        if (ObjectUtil.hasNull(userId, productId, quantity) || quantity <= 0) {
            throw new RuntimeException("参数错误");
        }
        
        Products product = productsMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        if (!product.getIsOnSale()) {
            throw new RuntimeException("商品已下架");
        }
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        Long cartId = getOrCreateCart(userId);
        
        CartItems existingItem = cartItemsMapper.selectByCartIdAndProductId(cartId, productId);
        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("库存不足");
            }
            existingItem.setQuantity(newQuantity);
            return cartItemsMapper.updateQuantity(existingItem) > 0;
        } else {
            CartItems cartItem = new CartItems();
            cartItem.setCartId(cartId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setAddedAt(LocalDateTime.now());
            return cartItemsMapper.insert(cartItem) > 0;
        }
    }
    
    @Override
    public boolean removeFromCart(Long userId, Long productId) {
        if (ObjectUtil.hasNull(userId, productId)) {
            throw new RuntimeException("参数错误");
        }
        
        Long cartId = shoppingCartsMapper.selectByUserId(userId);
        if (cartId == null) {
            return false;
        }
        
        return cartItemsMapper.deleteByCartIdAndProductId(cartId, productId) > 0;
    }
    
    @Override
    public boolean updateCartItemQuantity(Long userId, Long productId, Integer quantity) {
        if (ObjectUtil.hasNull(userId, productId, quantity) || quantity <= 0) {
            throw new RuntimeException("参数错误");
        }
        
        Products product = productsMapper.selectById(productId);
        if (product == null || !product.getIsOnSale()) {
            throw new RuntimeException("商品不存在或已下架");
        }
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        Long cartId = shoppingCartsMapper.selectByUserId(userId);
        if (cartId == null) {
            return false;
        }
        
        CartItems cartItem = cartItemsMapper.selectByCartIdAndProductId(cartId, productId);
        if (cartItem == null) {
            return false;
        }
        
        cartItem.setQuantity(quantity);
        return cartItemsMapper.updateQuantity(cartItem) > 0;
    }
    
    @Override
    public List<CartVO> getCartItems(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        Long cartId = shoppingCartsMapper.selectByUserId(userId);
        if (cartId == null) {
            return new ArrayList<>();
        }
        
        List<CartItems> cartItems = cartItemsMapper.selectByCartId(cartId);
        List<CartVO> cartVOs = new ArrayList<>();
        
        for (CartItems item : cartItems) {
            Products product = productsMapper.selectById(item.getProductId());
            if (product != null && product.getIsOnSale()) {
                CartVO cartVO = new CartVO();
                cartVO.setProductId(product.getId());
                cartVO.setProductName(product.getName());
                cartVO.setDescription(product.getDescription());
                cartVO.setPrice(product.getPrice());
                cartVO.setQuantity(item.getQuantity());
                cartVO.setTotalPrice(product.getPrice().multiply(new BigDecimal(item.getQuantity())));
                cartVOs.add(cartVO);
            }
        }
        
        return cartVOs;
    }
    
    @Override
    public boolean clearCart(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        Long cartId = shoppingCartsMapper.selectByUserId(userId);
        if (cartId == null) {
            return true;
        }
        
        return cartItemsMapper.deleteByCartId(cartId) >= 0;
    }
    
    @Override
    public CartVO checkout(Long userId) {
        if (userId == null) {
            throw new RuntimeException("用户ID不能为空");
        }
        
        List<CartVO> cartItems = getCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("购物车为空");
        }
        
        BigDecimal totalAmount = cartItems.stream()
            .map(CartVO::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        CartVO checkoutResult = new CartVO();
        checkoutResult.setTotalPrice(totalAmount);
        
        clearCart(userId);
        
        return checkoutResult;
    }
    
    private Long getOrCreateCart(Long userId) {
        Long cartId = shoppingCartsMapper.selectByUserId(userId);
        if (cartId == null) {
            ShoppingCarts cart = new ShoppingCarts();
            cart.setUserId(userId);
            cart.setCreatedAt(LocalDateTime.now());
            cart.setUpdatedAt(LocalDateTime.now());
            shoppingCartsMapper.insert(cart);
            cartId = cart.getId();
        }
        return cartId;
    }
}