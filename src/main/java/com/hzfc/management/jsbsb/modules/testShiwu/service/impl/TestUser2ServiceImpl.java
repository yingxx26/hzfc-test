package com.hzfc.management.jsbsb.modules.testShiwu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hzfc.management.jsbsb.modules.testShiwu.mapper.User2Mapper;
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
public class TestUser2ServiceImpl extends ServiceImpl<User2Mapper, User2> implements TestUser2Service {

    @Autowired
    private User2Mapper user2Mapper;

    @Override
    @Transactional
    public void testSaveUser2(User2 user2) {
        user2Mapper.insert(user2);
        int x = 1 / 0;
    }


}

