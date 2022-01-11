package com.hzfc.management.jsbsb.common.outapi;

/**
 * @Author yxx
 * @Date 2022/1/11 12:56
 */
public interface SignChecker {
    boolean check(String sourceContent, String signature, String signType, String charset);

    boolean checkCert(String sourceContent, String signature, String signType, String charset, String publicKey);
}
