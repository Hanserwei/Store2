package com.hanserwei.service.impl;

import com.hanserwei.entity.dto.UserLoginDTO;
import com.hanserwei.entity.dto.UserRegisterDTO;
import com.hanserwei.entity.vo.UserLoginVO;
import com.hanserwei.mapper.UsersMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

public class UserServiceImplTest {
    // 注入ServiceImpl
    private final UserServiceImpl userService;

    public UserServiceImplTest() {
        // 创建SqlSessionFactory对象
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        // 创建SqlSession对象
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        // 创建UsersMapper对象
        UsersMapper usersMapper = sqlSession.getMapper(UsersMapper.class);
        userService = new UserServiceImpl(usersMapper);
    }

    @Test
    public void userRegister() {
        boolean result = userService.userRegister(new UserRegisterDTO("Hanserwu", "wwgb1314", "wwgb1314", "ww1314@qq.com", "1399999999"));
        Assert.assertTrue(result);
    }

    @Test
    public void userLogin() {
        UserLoginDTO userLoginDTO = new UserLoginDTO("Hanserwei", "wwgb1314");
        UserLoginVO userLoginVO = userService.userLogin(userLoginDTO);
        Assert.assertNotNull(userLoginVO);
        System.out.println(userLoginVO);
    }
}