package com.hanserwei.service;

import com.hanserwei.entity.dto.FatherCategoryDTO;
import com.hanserwei.entity.vo.CategoryVO;
import com.hanserwei.entity.vo.ChildrenCategoryVO;
import com.hanserwei.entity.vo.FalterCategoryVO;

import java.util.List;

public interface CategoryService {
    // 查询所有分类
    List<CategoryVO> selectAllCategories();
    //查询父分类
    List<FalterCategoryVO> selectFalterCategories();

    Long addNewFatherCategory(String newFatherName);

    Long addNewChildCategory(Long newFatherId, String newChildName);

    List<ChildrenCategoryVO> selectAllChildren(Long fatherCategoryId);

    List<Long> selectAllChildrenIds(Long fatherCategoryId);
}
