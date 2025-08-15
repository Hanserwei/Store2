package com.hanserwei.mapper;

import com.hanserwei.entity.vo.CategoryVO;

import java.util.List;

public interface CategoriesMapper {
    List<CategoryVO> selectAll();
}