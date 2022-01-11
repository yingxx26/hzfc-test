package com.hzfc.management.jsbsb.common.outapi;


public interface JSONErrorListener {
    void start(String text);

    void error(String message, int column);

    void end();
}
