package com.hanserwei.service;

import com.hanserwei.entity.po.Products;
import com.hanserwei.entity.vo.ProductsVO;

import java.util.List;

public interface ProductService {
    List<ProductsVO> selectAll();
    
    List<ProductsVO> getAllProducts();
    //TODO:用户表应该加个权限字段，管理员可以调用下面的两个接口
    Products getProductById(Long id);
    
    boolean updateStock(Long productId, Integer quantity);
}
