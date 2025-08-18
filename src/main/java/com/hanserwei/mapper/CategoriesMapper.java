package com.hanserwei.mapper;

import com.hanserwei.entity.dto.ChildCategoryDTO;
import com.hanserwei.entity.dto.FatherCategoryDTO;
import com.hanserwei.entity.po.Categories;
import com.hanserwei.entity.vo.CategoryVO;
import com.hanserwei.entity.vo.ChildrenCategoryVO;
import com.hanserwei.entity.vo.FalterCategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoriesMapper {
    List<CategoryVO> selectAll();

    List<FalterCategoryVO> selectAllFatherCategories();

    Long selectFatherCategoryMaxId();

    Long insertOneFatherCategory(Categories fatherCategory);

    Long insertOneChildCategory(Categories childCategory);

    List<ChildrenCategoryVO> selectAllChildren(Long fatherCategoryId);

    List<Long> selectAllChildrenIds(Long fatherCategoryId);
}