package com.hzfc.management.jsbsb.modules.testresponsibilitychain2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author yxx
 * @Date 2021/3/1 14:45
 */
@Service
public class ResponsibilitychainManageer2 {


    private Handler firstHandler;

    private List<Handler> handlers;

    @Autowired
    public ResponsibilitychainManageer2(List<Handler> handlers) {
        this.handlers = handlers;
        this.firstHandler = this.handlers.stream().reduce(null, (a, b) -> {
            b.setSuccessor(a);
            return b;
        });
    }


    public String handlerFeeRequset(String user, double fee) {
        return this.firstHandler.handlerFee(user, fee);
    }

}
