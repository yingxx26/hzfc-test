package com.hzfc.management.jsbsb.rsa2;


import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

//import static com.globex.util.RSA2.KEY_ALGORITHM;

/**
 * Created by lijinquan on 2019/3/27.
 */
public class Rsatest {
    private static final Logger logger = LoggerFactory.getLogger(Rsatest.class);
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    public static final String ENCODE_ALGORITHM = "SHA-256";
    public static final String PLAIN_TEXT = "test string";
    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 245;
    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 256;

    private static Base64.Decoder decoder = Base64.getDecoder();
    private static Base64.Encoder encoder = Base64.getEncoder();

    public static String encode(byte[]   key) {

      return   encoder.encodeToString(key);
    }

    public static String decode2str( String keystr) throws UnsupportedEncodingException {

        return  new String(decoder.decode(keystr), "UTF-8");

    }

    public static byte[] decode( String keystr)  {

        return  decoder.decode(keystr) ;

    }

    public static void main(String[] args) throws Exception {

        Rsatest rsa2Util = new Rsatest();
        String str1 = "{\"content\":\"{\\\"orderProducts\\\":[{\\\"productSku\\\":\\\"\\\",\\\"productBarcode\\\":\\\"\\\",\\\"productTitle\\\":\\\"LED Stage Lighting \\\\/ LED PAR Light \\\\/ Stage Truss Light (Parco 9D) \\\\/d\\\",\\\"opQuantity\\\":3,\\\"opValue\\\":200,\\\"opValueTotal\\\":600}],\\\"plateCode\\\":\\\"lightrade\\\",\\\"referenceNo\\\":\\\"9928818286207\\\",\\\"sellerCode\\\":\\\"SE0226\\\",\\\"orderAmount\\\":700,\\\"salesAmount\\\":600,\\\"goodsValue\\\":600,\\\"orderCurrency\\\":\\\"USD\\\",\\\"smCode\\\":\\\"HKDHL\\\",\\\"returnUrl\\\":\\\"http:\\\\/\\\\/www.zhaoming123.com\\\\/Views\\\\/Order\\\\/PayResult.aspx\\\",\\\"notifyUrl\\\":\\\"http:\\\\/\\\\/www.zhaoming123.com\\\\/Views\\\\/Order\\\\/referenceN.aspx\\\",\\\"orderType\\\":\\\"1\\\",\\\"payType\\\":\\\"2\\\",\\\"plateformFee\\\":0,\\\"logisticsFee\\\":100,\\\"globexFee\\\":0,\\\"eTradePayFee\\\":0,\\\"smType\\\":\\\"HY\\\",\\\"note\\\":\\\"\\\",\\\"deleteOld\\\":1,\\\"deposit\\\":500,\\\"finalPayment\\\":200}\",\"timestamp\":1554898083,\"version\":\"1.0.0\"}";
        System.out.println("明文==>" + str1);

        // 公私钥对
        Map<String, byte[]> keyMap = RSA2.generateKeyBytes();
        String publicKey =  encode(keyMap.get(RSA2.PUBLIC_KEY));

        System.out.println("验签公钥==>" + publicKey);

        String privateKey =  encode(keyMap.get(RSA2.PRIVATE_KEY));

        System.out.println("签名私钥==>" + privateKey);

//
//        String pem = RSA2.privatePem(privateKey);
//        System.out.println("openssl 生成的私钥.pem:");
//        System.out.println(pem);
//
//        System.out.println("openssl 生成的公钥.pem");
//        System.out.println("-----BEGIN PUBLIC KEY-----");
//        RSA2.formatKey(publicKey.replace("_","/").replace("-","+"));
//        System.out.println("-----END PUBLIC KEY-----");
//

        // 签名加密
        Map maps = (Map) JSON.parse(str1);
        // maps.remove("sign");

        List<String> paramsKeyList = rsa2Util.getParamsKeyList(maps);
        String content = rsa2Util.splitParams(paramsKeyList, maps);
        System.out.println("规则处理后=" + content);
        content = "content{\"orderProducts\":[{\"productSku\":\"\",\"productBarcode\":\"\",\"productTitle\":\"LED Stage Lighting \\/ LED PAR Light \\/ Stage Truss Light (Parco 9D) \\/d\",\"opQuantity\":3,\"opValue\":200,\"opValueTotal\":600}],\"plateCode\":\"lightrade\",\"referenceNo\":\"9928818286207\",\"sellerCode\":\"SE0226\",\"orderAmount\":700,\"salesAmount\":600,\"goodsValue\":600,\"orderCurrency\":\"USD\",\"smCode\":\"HKDHL\",\"returnUrl\":\"http:\\/\\/www.zhaoming123.com\\/Views\\/Order\\/PayResult.aspx\",\"notifyUrl\":\"http:\\/\\/www.zhaoming123.com\\/Views\\/Order\\/referenceN.aspx\",\"orderType\":\"1\",\"payType\":\"2\",\"plateformFee\":0,\"logisticsFee\":100,\"globexFee\":0,\"eTradePayFee\":0,\"smType\":\"HY\",\"note\":\"\",\"deleteOld\":1,\"deposit\":500,\"finalPayment\":200}timestamp1554898083version1.0.0";
        String signstr = rsa2Util.signStr(privateKey, content);
        System.out.println("签名后的数据=" + signstr);


        boolean b = rsa2Util.verifySignByMap(publicKey, maps, signstr);
        System.out.println("验签结果=" + b);

        try {

            String enpublicKey =  encode(keyMap.get(RSA2.PUBLIC_KEY));

            System.out.println("加密公钥=" + enpublicKey);

            String deprivateKey =  encode(keyMap.get(RSA2.PRIVATE_KEY));
            System.out.println("解密私钥=" + deprivateKey);
            //String encode = rsa2Util.encrypt(str1, enpublicKey);

            String encode = rsa2Util.sectionEncrypt(str1, enpublicKey);
            System.out.println("明文加密后==>" + encode);
            // String decode = rsa2Util.decrypt(encode, deprivateKey);
            String decode = rsa2Util.sectionDecrypt(encode, deprivateKey);
            System.out.println("解密后==>" + decode);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解密后验签
     *
     * @param datalist  排序后的明文
     * @param signList  加签后的数据
     * @param publicKey 解签用公钥
     * @return
     */
    public boolean checkSign(List<String> datalist, List<String> signList, String publicKey) {

        StringBuffer sb = new StringBuffer();
        int size = datalist.size();
        for (int i = 0; i < size; i++) {
            boolean b = verifySignStr(publicKey, datalist.get(i), signList.get(i));
            if (!b) {
                return b;
            }
        }

        return true;
    }


    /*****************************       RSA256 公钥加密 私钥解密       *********************************/

    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     */

    public String encrypt(String data, String publicKey) {
        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec( decode(publicKey));
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return  encode(cipher.doFinal(data.getBytes()));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p>
     * 私钥加密
     * </p >
     *
     * @param content    源数据
     * @param privateKey 私钥(BASE64编码)
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String content, String privateKey)
            throws Exception {
        Key privateK = RSA2.restorePrivateKey( decode(privateKey));
        Cipher cipher = Cipher.getInstance("RSA");/////
        cipher.init(Cipher.ENCRYPT_MODE, privateK);
        byte[] data = content.getBytes();
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return new String( encode(encryptedData));
    }


    public String sectionEncrypt(String data, String publicKey) {

        try {
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec pub_spec = new X509EncodedKeySpec( decode(publicKey));
            PublicKey pubKey = mykeyFactory.generatePublic(pub_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            byte[] bytesData = data.getBytes();
            int inputLen = bytesData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(bytesData, offSet, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(bytesData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            out.close();
            return  encode(encryptedData);

        } catch (Exception e) {
            return null;
        }

    }


    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     */
    public String decrypt(String data, String privateKey) throws Exception {
        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec( decode(privateKey));
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privKey);
            return new String(cipher.doFinal( decode(data)));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("解密失败！");
        }
    }


    /***
     * 分段解密
     */
    public String sectionDecrypt(String data, String privateKey) throws Exception {

        try {
            PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec( decode(privateKey));
            KeyFactory mykeyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privKey = mykeyFactory.generatePrivate(priv_spec);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privKey);

            byte[] encryptedData =  decode(data);

            int inputLen = encryptedData.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            out.close();
            return new String(decryptedData);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("解密失败！");
            return null;
        }

    }


    /*****************************       RSA256 私钥 签名        *********************************/

    /**
     * 将Map的key-value排序后, 进行签名
     *
     * @param privateKey
     * @param maps
     * @return 返回签名后的字符串
     */
    public String signByMap(String privateKey, Map<String, Object> maps) {
        String plainText = splitParams(getParamsKeyList(maps), maps);
        return signStr(privateKey, plainText);
    }

    /**
     * 签名将值转换为字符串
     *
     * @param privateKey
     * @param plainText
     * @return
     */
    public String signStr(String privateKey, String plainText) {
        return bytesToHexString(sign(RSA2.restorePrivateKey( decode(privateKey)), plainText));
    }


    /**
     * 将Map的key-value排序后, 验证签名字符串是否匹配
     *
     * @param publicKey
     * @param maps
     * @param signValue
     * @return
     */
    public boolean verifySignByMap(String publicKey, Map<String, Object> maps, String signValue) {
        String plainText = splitParams(getParamsKeyList(maps), maps);
        logger.info("签名前排序==========>" + plainText);
        return verifySignStr(publicKey, plainText, signValue);
    }

    /**
     * 验证签名字符串是否匹配
     *
     * @param publicKey
     * @param plainText
     * @param signValue
     * @return
     */
    public boolean verifySignStr(String publicKey, String plainText, String signValue) {
        return verifySign(RSA2.restorePublicKey( decode(publicKey)), plainText, hexStringToBytes(signValue));
    }


    /**
     * 签名
     *
     * @param privateKey 私钥
     * @param plain_text 明文
     * @return
     */
    public byte[] sign(PrivateKey privateKey, String plain_text) {
        MessageDigest messageDigest;
        byte[] signed = null;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plain_text.getBytes());
            byte[] outputDigest_sign = messageDigest.digest();
            //System.out.println("SHA-256加密后-----》" +bytesToHexString(outputDigest_sign));
            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            Sign.initSign(privateKey);
            Sign.update(outputDigest_sign);
            signed = Sign.sign();
//            System.out.println("SHA256withRSA签名后-----》" + bytesToHexString(signed));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return signed;
    }

    /**
     * 验签
     *
     * @param publicKey  公钥
     * @param plain_text 明文
     * @param signed     签名
     */
    public boolean verifySign(PublicKey publicKey, String plain_text, byte[] signed) {

        MessageDigest messageDigest;
        boolean SignedSuccess = false;
        try {
            messageDigest = MessageDigest.getInstance(ENCODE_ALGORITHM);
            messageDigest.update(plain_text.getBytes("UTF-8"));
            byte[] outputDigest_verify = messageDigest.digest();
//            System.out.println("SHA-256加密后-----》" +bytesToHexString(outputDigest_verify));
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            verifySign.initVerify(publicKey);
            verifySign.update(outputDigest_verify);
            SignedSuccess = verifySign.verify(signed);
//            System.out.println("验证成功？---" + SignedSuccess);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return SignedSuccess;
    }

    /**
     * bytes[]换成16进制字符串
     *
     * @param src
     * @return
     */
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


//    /**
//     * 将16进制字符串转换为byte[]
//     * @param hexString
//     * @return
//     */
//    public  byte[] hexStringToBytes(String hexString) {
//        if (hexString == null || hexString.equals("")) {
//            return null;
//        }
//        hexString = hexString.toUpperCase();
//        int length = hexString.length() / 2;
//        char[] hexChars = hexString.toCharArray();
//        byte[] d = new byte[length];
//        for (int i = 0; i < length; i++) {
//            int pos = i * 2;
//            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
//        }
//        return d;
//    }


    public byte[] hexStringToBytes(String s) {

        System.out.println("hexStringToBytes==>" + s);
        byte[] bytes;

        bytes = new byte[s.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16);
        }

        return bytes;
    }


    private byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 拼接排序好的参数名称和参数值 拼接格式为  [参数名a][参数值a][参数名b][参数值b].....
     *
     * @param keys 排序后的参数名称集合
     * @param maps 参数key-value map集合
     * @return String 拼接后的字符串
     */
    private String splitParams(List<String> keys, Map<String, Object> maps) {
        StringBuilder paramStr = new StringBuilder();
        for (String paramName : keys) {
            paramStr.append(paramName);
            if (maps.containsKey(paramName)) {
                paramStr.append(String.valueOf(maps.get(paramName)));
            }
        }
        return paramStr.toString();
    }

    /**
     * 获取参数名称 key 的 List列表, 并且将 参数名称按字典排序
     *
     * @param maps 参数key-value map集合
     * @return
     */
    public List<String> getParamsKeyList(Map<String, Object> maps) {
        List<String> keys = new ArrayList<String>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            keys.add(entry.getKey());
        }
        return lexicographicOrder(keys);
    }

    /**
     * 参数名称按字典排序
     *
     * @param keys 参数名称List集合
     * @return 排序后的参数名称List集合
     */
    public List<String> lexicographicOrder(List<String> keys) {
        Collections.sort(keys);
        return keys;
    }
}
