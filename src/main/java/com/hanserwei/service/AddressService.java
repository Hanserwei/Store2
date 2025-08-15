package com.hanserwei.service;

import com.hanserwei.entity.po.Addresses;
import com.hanserwei.entity.vo.AddressVO;

import java.util.List;

public interface AddressService {
    List<AddressVO> selectAll(Long userId);
    
    List<AddressVO> getAddressesByUserId(Long userId);
    
    boolean addAddress(Addresses address);
    
    boolean updateAddress(Addresses address);
    
    boolean deleteAddress(Long addressId, Long userId);
    
    boolean setDefaultAddress(Long addressId, Long userId);
}
