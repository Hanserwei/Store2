package com.hanserwei.mapper;

import com.hanserwei.entity.po.Users;
import com.hanserwei.entity.vo.UserLoginVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UsersMapper {
    Long selectOneByUsername(String username);

    Long insertOneUser(Users users);

    UserLoginVO selectByUsernameAndPasswordHash(@Param("username") String username, @Param("passwordHash") String passwordHash);

    String selectOneByEmail(String email);

    String selectOneByPhoneNumber(String phoneNumber);
}