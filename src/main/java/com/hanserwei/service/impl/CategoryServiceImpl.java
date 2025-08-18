package com.hanserwei.service.impl;

import com.hanserwei.entity.po.Categories;
import com.hanserwei.entity.vo.CategoryVO;
import com.hanserwei.entity.vo.ChildrenCategoryVO;
import com.hanserwei.entity.vo.FalterCategoryVO;
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

    @Override
    public List<FalterCategoryVO> selectFalterCategories() {
        return categoriesMapper.selectAllFatherCategories();
    }

    @Override
    public Long addNewFatherCategory(String newFatherName) {
        // 先查询父分类最大id,其实可以不查，让数据库自增
        // Long maxFatherId = categoriesMapper.selectFatherCategoryMaxId();
        Categories fatherCategory = new Categories(null, newFatherName, null);
        // 插入
        Long result = categoriesMapper.insertOneFatherCategory(fatherCategory);
        if (result != 0) {
            return fatherCategory.getId();
        } else {
            throw new RuntimeException("添加父分类失败！");
        }
    }

    @Override
    public Long addNewChildCategory(Long newFatherId, String newChildName) {
        Categories childCategory = new Categories(null, newChildName, newFatherId);
        Long result = categoriesMapper.insertOneChildCategory(childCategory);
        if (result != 0) {
            return childCategory.getId();
        } else {
            throw new RuntimeException("添加子分类失败！");
        }
    }

    @Override
    public List<ChildrenCategoryVO> selectAllChildren(Long fatherCategoryId) {
        return categoriesMapper.selectAllChildren(fatherCategoryId);
    }

    @Override
    public List<Long> selectAllChildrenIds(Long fatherCategoryId) {
        return categoriesMapper.selectAllChildrenIds(fatherCategoryId);
    }
}
