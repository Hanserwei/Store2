package com.hanserwei.mapper;

import com.hanserwei.entity.vo.ChildrenCategoryVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;

class CategoriesMapperTest {


    @Test
    void selectAll() {
    }

    @Test
    void selectAllFatherCategories() {
    }

    @Test
    void selectFatherCategoryMaxId() {
    }

    @Test
    void insertOneFatherCategory() {
        // 创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        // 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // CategoriesMapper categoriesMapper = sqlSession.getMapper(CategoriesMapper.class);
        // categoriesMapper.insertOneFatherCategory(new FatherCategoryDTO(999L, "测试"));
    }

    @Test
    void insertOneChildCategory() {
    }


    @Test
    void selectAllChildren() {
        // 创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        // 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        CategoriesMapper categoriesMapper = sqlSession.getMapper(CategoriesMapper.class);
        List<ChildrenCategoryVO> childrenCategoryVOList = categoriesMapper.selectAllChildren(1L);
        childrenCategoryVOList.forEach(System.out::println);
    }

    @Test
    void selectAllChildrenIds() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        // 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        CategoriesMapper categoriesMapper = sqlSession.getMapper(CategoriesMapper.class);
        List<Long> childrenIds = categoriesMapper.selectAllChildrenIds(1L);
        childrenIds.forEach(System.out::println);
    }
}