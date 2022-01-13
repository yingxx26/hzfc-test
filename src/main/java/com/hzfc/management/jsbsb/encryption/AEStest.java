package com.hzfc.management.jsbsb.encryption;

import org.apache.commons.codec.binary.Base64;

/**
 * @Author yxx
 * @Date 2022/1/12 19:59
 */
public class AEStest {

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        byte[] key = AESUtils.initkey();
        String encodeBase64String = Base64.encodeBase64String(key);
        // 初始化密钥
        //byte[] key = Base64.decodeBase64("6M1133yyTothav0APxDAd+C4TFkDektkLgFcCjlQx11=");// AESCoder.initkey();
        System.out.println("密钥：" + Base64.encodeBase64String(key));
        byte[] bytes1 = Base64.decodeBase64(encodeBase64String);
        for (int i = 0; i < 100; i++) {
            String str = "我是一个快乐的人-" + i;//我是一个快乐的人-0
            System.out.println("原文：" + str);
            // 加密数据
            byte[] data = AESUtils.encrypt(str.getBytes(), bytes1);
            System.out.println("加密后：" + Base64.encodeBase64String(data));
            String s = Base64.encodeBase64String(data);
            byte[] bytes = Base64.decodeBase64(s);
            // 解密数据
            data = AESUtils.decrypt(data, bytes1);
            System.out.println("解密后：" + new String(data));
            System.out.println("========================");
        }
    }
}
