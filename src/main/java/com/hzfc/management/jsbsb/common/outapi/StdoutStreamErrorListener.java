package com.hzfc.management.jsbsb.common.outapi;


public class StdoutStreamErrorListener extends BufferErrorListener {

    public void end() {
        System.out.print(buffer.toString());
    }
}
