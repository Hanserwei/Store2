package com.hanserwei.entity.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserLoginVO implements Serializable {

    @Serial
    private static final long serialVersionUID = -8430923073113971915L;

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户手机号
     */
    private String phoneNumber;

}
