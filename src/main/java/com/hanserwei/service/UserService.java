package com.hanserwei.service;

import com.hanserwei.entity.dto.UserLoginDTO;
import com.hanserwei.entity.dto.UserRegisterDTO;
import com.hanserwei.entity.vo.UserLoginVO;

import java.math.BigDecimal;

public interface UserService {

    boolean userRegister(UserRegisterDTO userRegisterDTO);

    UserLoginVO userLogin(UserLoginDTO userLoginDTO);

    BigDecimal getUserBalance(Long userId);
}
