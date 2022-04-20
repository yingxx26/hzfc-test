package com.hzfc.management.jsbsb.modules.testShiwu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User;
import com.hzfc.management.jsbsb.modules.testShiwu.model.User2;
import org.apache.ibatis.annotations.Select;

public interface TestUserService extends IService<User> {

    void testSaveUser(User user);

    void testSaveUserCopy(User user);



}
