package com.hzfc.management.jsbsb.modules.testShiwu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User2;

public interface TestUser2Service extends IService<User2> {

    void testSaveUser2(User2 user2);
}
