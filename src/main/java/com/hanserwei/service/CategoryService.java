package com.hanserwei.service;

import com.hanserwei.entity.vo.CategoryVO;

import java.util.List;

public interface CategoryService {
    // 查询所有分类
    List<CategoryVO> selectAllCategories();
}
