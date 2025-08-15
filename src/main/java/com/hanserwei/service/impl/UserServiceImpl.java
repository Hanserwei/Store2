package com.hanserwei.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.hanserwei.entity.dto.UserLoginDTO;
import com.hanserwei.entity.dto.UserRegisterDTO;
import com.hanserwei.entity.po.Users;
import com.hanserwei.entity.vo.UserLoginVO;
import com.hanserwei.mapper.UsersMapper;
import com.hanserwei.service.UserService;

import java.time.LocalDateTime;

public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;

    public UserServiceImpl(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Override
    public boolean userRegister(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.username();
        String password = userRegisterDTO.password();
        String email = userRegisterDTO.email();
        String phoneNumber = userRegisterDTO.phoneNumber();
        String checkPassword = userRegisterDTO.checkPassword();
        if (StrUtil.hasBlank(username, password, email, phoneNumber, checkPassword)) {
            throw new RuntimeException("参数错误");
        }
        if (!password.equals(checkPassword)) {
            throw new RuntimeException("密码不一致");
        }
        Long userId = usersMapper.selectOneByUsername(username);
        String selectedEmail = usersMapper.selectOneByEmail(email);
        String selectedPhoneNumber = usersMapper.selectOneByPhoneNumber(phoneNumber);
        if (userId != null) {
            throw new RuntimeException("用户已存在");
        }
        if (selectedEmail != null) {
            throw new RuntimeException("邮箱已存在");
        }
        if (selectedPhoneNumber != null) {
            throw new RuntimeException("手机号已存在");
        }
        Users user = new Users();
        // 加密
        String passwordMd5 = SecureUtil.md5(password);
        user.setUsername(username);
        user.setPasswordHash(passwordMd5);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setCreatedAt(LocalDateTime.now());
        Long id = usersMapper.insertOneUser(user);
        return id != null;
    }

    @Override
    public UserLoginVO userLogin(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.username();
        String password = userLoginDTO.password();
        if (StrUtil.hasBlank(username, password)) {
            throw new RuntimeException("参数错误");
        }
        UserLoginVO userLoginVO = usersMapper.selectByUsernameAndPasswordHash(username, SecureUtil.md5(password));
        if (userLoginVO == null) {
            throw new RuntimeException("用户或密码错误");
        }
        return userLoginVO;
    }
}
