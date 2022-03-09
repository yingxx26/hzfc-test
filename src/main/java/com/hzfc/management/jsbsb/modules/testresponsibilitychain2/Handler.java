package com.hzfc.management.jsbsb.modules.testresponsibilitychain2;

public abstract class Handler {

    protected Handler successor = null;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public abstract String handlerFeeRequset(String user, double fee);

    protected abstract boolean supports(String user, double fee);

    public String handlerFee(String user, double fee) {
        if (this.supports(user, fee)) {
            return this.handlerFeeRequset(user, fee);
        } else if (this.successor != null) {
            return this.successor.handlerFeeRequset(user, fee);
        }

        // 不支持的
        throw new RuntimeException("房源状态变更失败: 不支持的状态");
    }

}
