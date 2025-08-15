package com.hanserwei.mapper;

import com.hanserwei.entity.po.Products;
import com.hanserwei.entity.vo.ProductsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductsMapper {
    List<ProductsVO> selectAll();
    
    Products selectById(Long id);
    
    int updateStock(@Param("id") Long id, @Param("stock") Integer stock);
}