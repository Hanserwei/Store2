package com.hanserwei.mapper;

import com.hanserwei.entity.vo.CategoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoriesMapper {
    List<CategoryVO> selectAll();
}