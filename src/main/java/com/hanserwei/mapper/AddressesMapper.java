package com.hanserwei.mapper;

import com.hanserwei.entity.po.Addresses;
import com.hanserwei.entity.vo.AddressVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressesMapper {
    List<AddressVO> selectAll(Long userId);

    String selectAllByUserIdAndIsDefault(Long userId);
    
    int insert(Addresses address);
    
    int update(Addresses address);
    
    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    int clearDefaultByUserId(Long userId);
    
    int setDefaultById(@Param("id") Long id, @Param("userId") Long userId);

    String selectDefaultAddress(Long userId);
}