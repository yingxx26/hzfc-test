package com.hzfc.management.jsbsb.common.outapi;

/**
 * @Author yxx
 * @Date 2022/1/11 12:58
 */
public interface Decryptor {
    /**
     * @param encryptContent
     * @param encryptType
     * @param charset
     * @return
     */
    String decrypt(String encryptContent, String encryptType, String charset);
}
