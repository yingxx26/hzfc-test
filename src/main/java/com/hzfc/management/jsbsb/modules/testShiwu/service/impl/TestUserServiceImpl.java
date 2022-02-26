package com.hzfc.management.jsbsb.modules.testShiwu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.jsbsb.modules.testShiwu.mapper.UserMapper;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User2;
import com.hzfc.management.jsbsb.modules.testShiwu.service.TestUser2Service;
import com.hzfc.management.jsbsb.modules.testShiwu.service.TestUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 后台管理员管理Service实现类
 * Created by hzfc on 2018/4/26.
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<UserMapper, User> implements TestUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TestUser2Service testUser2Service;

    @Override
    //@Transactional
    public void testSaveUser(User user) {
        userMapper.insert(user);
        testSaveUserCopy(user);
    }

    @Override
    @Transactional
    public void testSaveUserCopy(User user) {
        userMapper.insert(user);
        int x = 1 / 0;
    }
}

