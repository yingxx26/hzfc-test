package com.hzfc.management.jsbsb.common.outapi;


/**
 * @author liuqun.lq
 * @version $Id: Signer.java, v 0.1 2018��07��03�� 11:36 liuqun.lq Exp $
 */
public interface Signer {

    String sign(String sourceContent, String signType, String charset);
}
