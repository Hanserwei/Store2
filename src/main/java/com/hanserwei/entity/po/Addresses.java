package com.hanserwei.entity.po;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 地址表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Addresses implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 地址唯一标识
     */
    private Long id;

    /**
     * 关联到users表的用户ID
     */
    private Long userId;

    /**
     * 收货人姓名
     */
    private String consignee;

    /**
     * 收货人电话
     */
    private String phoneNumber;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区/县
     */
    private String district;

    /**
     * 详细街道地址
     */
    private String street;

    /**
     * 是否为默认地址，0否 1是
     */
    private Boolean isDefault;
}