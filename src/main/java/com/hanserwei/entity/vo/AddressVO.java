package com.hanserwei.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1309349229051619801L;

    /**
     * 地址ID
     */
    private Long id;

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
     * 收货人详细地址
     */
    private String detailedAddress;

    /**
     * 是否为默认地址，0否 1是
     */
    private Boolean isDefault;
}
