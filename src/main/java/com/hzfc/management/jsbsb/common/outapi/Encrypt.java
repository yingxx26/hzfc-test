package com.hzfc.management.jsbsb.common.outapi;

/**
 * @author zts
 * @version : Encrypt.java, v 0.1 2021年06月23日 3:53 PM zts Exp $
 */
public interface Encrypt {


    String encrypt(String content, String aesKey, String charset) throws AlipayApiException;

    String decrypt(String content, String key, String charset) throws AlipayApiException;

}
