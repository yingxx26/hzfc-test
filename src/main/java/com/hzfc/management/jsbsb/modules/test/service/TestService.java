package com.hzfc.management.jsbsb.modules.test.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hzfc.management.jsbsb.modules.ums.dto.UmsAdminParam;
import com.hzfc.management.jsbsb.modules.ums.dto.UpdateAdminPasswordParam;
import com.hzfc.management.jsbsb.modules.ums.model.UmsAdmin;
import com.hzfc.management.jsbsb.modules.ums.model.UmsResource;
import com.hzfc.management.jsbsb.modules.ums.model.UmsRole;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TestService {

    void test();
}
