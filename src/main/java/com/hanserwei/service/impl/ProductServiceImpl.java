package com.hanserwei.service.impl;

import com.hanserwei.entity.po.Products;
import com.hanserwei.entity.vo.ProductsVO;
import com.hanserwei.mapper.ProductsMapper;
import com.hanserwei.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private final ProductsMapper productsMapper;

    public ProductServiceImpl(ProductsMapper productsMapper) {
        this.productsMapper = productsMapper;
    }

    @Override
    public List<ProductsVO> selectAll() {
        return productsMapper.selectAll();
    }
    
    @Override
    public List<ProductsVO> getAllProducts() {
        return productsMapper.selectAll();
    }
    
    @Override
    public Products getProductById(Long id) {
        return productsMapper.selectById(id);
    }
    
    @Override
    public boolean updateStock(Long productId, Integer quantity) {
        if (productId == null || quantity == null || quantity < 0) {
            throw new RuntimeException("参数错误");
        }
        
        Products product = productsMapper.selectById(productId);
        if (product == null) {
            throw new RuntimeException("商品不存在");
        }
        
        if (product.getStock() < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        int newStock = product.getStock() - quantity;
        return productsMapper.updateStock(productId, newStock) > 0;
    }
}
