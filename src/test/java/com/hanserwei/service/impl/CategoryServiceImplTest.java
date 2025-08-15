package com.hanserwei.service.impl;

import cn.hutool.json.JSONUtil;
import com.hanserwei.mapper.CategoriesMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

public class CategoryServiceImplTest {

    private final CategoryServiceImpl categoryService;

    public CategoryServiceImplTest() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        // 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        CategoriesMapper categoriesMapper = sqlSession.getMapper(CategoriesMapper.class);
        categoryService = new CategoryServiceImpl(categoriesMapper);
    }

    @Test
    public void selectAllCategories() {
        System.out.println(JSONUtil.parse(categoryService.selectAllCategories()));
        System.out.println(categoryService.selectAllCategories());
    }
}