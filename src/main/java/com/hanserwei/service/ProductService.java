package com.hanserwei.service;

import com.hanserwei.entity.po.Products;
import com.hanserwei.entity.vo.ProductsVO;

import java.util.List;

public interface ProductService {
    List<ProductsVO> selectAll();
    
    List<ProductsVO> getAllProducts();

    Products getProductById(Long id);

    List<ProductsVO> selectAllProducts();

    Long addOneProduct(Products products);

    void updateProduct(Products product);

    void offShelfProduct(Long productId);

    void deleteOneProduct(Long productId);
}
