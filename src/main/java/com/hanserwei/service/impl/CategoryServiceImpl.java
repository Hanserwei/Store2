package com.hanserwei.service.impl;

import com.hanserwei.entity.vo.CategoryVO;
import com.hanserwei.mapper.CategoriesMapper;
import com.hanserwei.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {

    private final CategoriesMapper categoriesMapper;

    public CategoryServiceImpl(CategoriesMapper categoriesMapper) {
        this.categoriesMapper = categoriesMapper;
    }

    @Override
    public List<CategoryVO> selectAllCategories() {
        return categoriesMapper.selectAll();
    }
}
