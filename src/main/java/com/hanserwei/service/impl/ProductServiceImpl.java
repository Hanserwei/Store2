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
    public List<ProductsVO> selectAllProducts() {
        return productsMapper.selectAllOrderByStock();
    }

    @Override
    public Long addOneProduct(Products products) {
        return productsMapper.addOneProduct(products);
    }

    @Override
    public void updateProduct(Products product) {
        productsMapper.updateOneProduct(product);
    }

    @Override
    public void offShelfProduct(Long productId) {
        if (productsMapper.selectById(productId).getIsOnSale() == true) {
            productsMapper.offShelfProduct(productId);
        }else {
            throw new RuntimeException("商品不存在或已下架");
        }
    }

    @Override
    public void deleteOneProduct(Long productId) {
        if (productsMapper.selectById(productId) != null){
            productsMapper.deleteOneProduct(productId);
        }else {
            throw new RuntimeException("商品不存在");
        }
    }
}
