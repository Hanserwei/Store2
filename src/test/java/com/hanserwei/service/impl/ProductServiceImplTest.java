package com.hanserwei.service.impl;

import cn.hutool.json.JSONUtil;
import com.hanserwei.mapper.ProductsMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProductServiceImplTest {
    ProductServiceImpl productService;

    public ProductServiceImplTest() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        productService = new ProductServiceImpl(sqlSession.getMapper(ProductsMapper.class));
    }

    @Test
    public void selectAll() {
        System.out.println(productService.selectAll());
        System.out.println(JSONUtil.parse(productService.selectAll()));
    }
}