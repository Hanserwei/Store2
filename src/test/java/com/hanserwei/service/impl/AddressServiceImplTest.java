package com.hanserwei.service.impl;

import cn.hutool.json.JSONUtil;
import com.hanserwei.entity.vo.AddressVO;
import com.hanserwei.mapper.AddressesMapper;
import com.hanserwei.service.AddressService;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.util.List;

public class AddressServiceImplTest {
    private AddressService addressService;

    public AddressServiceImplTest() {
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().
                build(this.getClass().getClassLoader().getResourceAsStream("mybatis-config.xml"));
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        addressService = new AddressServiceImpl(sqlSession.getMapper(AddressesMapper.class));
    }

    @Test
    public void selectAll() {
        List<AddressVO> addressVOS = addressService.selectAll(2L);
        System.out.println(addressVOS);
        System.out.println(JSONUtil.parse(addressVOS));
    }
}