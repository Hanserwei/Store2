package com.hanserwei.service.impl;

import com.hanserwei.entity.po.Addresses;
import com.hanserwei.entity.vo.AddressVO;
import com.hanserwei.mapper.AddressesMapper;
import com.hanserwei.service.AddressService;

import java.util.List;

public class AddressServiceImpl implements AddressService {
    private final AddressesMapper addressesMapper;

    public AddressServiceImpl(AddressesMapper addressesMapper) {
        this.addressesMapper = addressesMapper;
    }

    @Override
    public List<AddressVO> selectAll(Long userId) {
        return addressesMapper.selectAll(userId);
    }
    
    @Override
    public List<AddressVO> getAddressesByUserId(Long userId) {
        return addressesMapper.selectAll(userId);
    }
    
    @Override
    public boolean addAddress(Addresses address) {
        if (address == null || address.getUserId() == null) {
            throw new RuntimeException("参数错误");
        }
        return addressesMapper.insert(address) > 0;
    }
    
    @Override
    public boolean updateAddress(Addresses address) {
        if (address == null || address.getId() == null || address.getUserId() == null) {
            throw new RuntimeException("参数错误");
        }
        return addressesMapper.update(address) > 0;
    }
    
    @Override
    public boolean deleteAddress(Long addressId, Long userId) {
        if (addressId == null || userId == null) {
            throw new RuntimeException("参数错误");
        }
        return addressesMapper.deleteByIdAndUserId(addressId, userId) > 0;
    }
    
    @Override
    public boolean setDefaultAddress(Long addressId, Long userId) {
        if (addressId == null || userId == null) {
            throw new RuntimeException("参数错误");
        }
        
        addressesMapper.clearDefaultByUserId(userId);
        
        return addressesMapper.setDefaultById(addressId, userId) > 0;
    }
}
