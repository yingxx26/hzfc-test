package com.hzfc.management.jsbsb.modules.testresponsibilitychain2;

import org.springframework.stereotype.Component;

@Component("ProjectManager2")
public class ProjectManager extends Handler {
    @Override
    public String handlerFeeRequset(String user, double fee) {
        String str = null;
        if ("小李".equals(user)) {
            str = "项目经理同意" + user + "聚餐费用" + fee + "元的请求";
        } else {
            str = "项目经理不同意" + user + "聚餐费用" + fee + "元的请求";
        }
        return str;

    }

    @Override
    protected boolean supports(String user, double fee) {
        return fee < 500;
    }
}
