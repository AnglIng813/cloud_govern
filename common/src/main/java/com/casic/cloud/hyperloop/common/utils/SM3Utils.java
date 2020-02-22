package com.casic.cloud.hyperloop.common.utils;


import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.digests.SM3Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.Arrays;

/***
 * @Description: sm3加密算法工具类
 * @Date: 2019/7/31 16:09
 */
public class SM3Utils {

    private static final String ENCODING = "UTF-8";
    private static final String SALT = "HT706";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * @param srcData 待加密字符串
     * @return 返回加密后，固定长度32位的16进制字符串
     * @Description: sm3加密算法
     * @Date: 2019/7/31 16:52
     */
    public static String encrypt(String srcData) {
        //16进制hash值
        String resultHexString = "";
        try {
            byte[] bytesData = srcData.getBytes(ENCODING);
            byte[] resultHash = hash(bytesData);
            //转16进制
            resultHexString = ByteUtils.toHexString(resultHash);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return resultHexString;
    }

    /**
     * @param key     盐值
     * @param srcData 待加密字符串
     * @return 返回加密后，固定长度32位的16进制字符串
     * @Description: sm3加密算法(加盐)
     * 默认盐值ht706
     * @Date: 2019/8/1 9:59
     */
    public static String encryptWithKey(String srcData, byte... key) {
        if (key.length <= 0) {
            key = SALT.getBytes();
        }

        String resultHexString = null;
        try {
            byte[] bytesData = srcData.getBytes(ENCODING);
            SM3Digest sm3Digest = new SM3Digest();
            HMac hMac = new HMac(sm3Digest);
            KeyParameter keyParameter = new KeyParameter(key);
            hMac.init(keyParameter);
            hMac.update(bytesData, 0, bytesData.length);
            byte[] result = new byte[hMac.getMacSize()];
            hMac.doFinal(result, 0);
            //转16进制
            resultHexString = ByteUtils.toHexString(result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return resultHexString;
    }

    /**
     * @Description: 生成对应hash值，填充长度32位的byte数组并返回
     * @Date: 2019/8/1 9:43
     */
    private static byte[] hash(byte[] srcData) {
        SM3Digest digest = new SM3Digest();
        digest.update(srcData, 0, srcData.length);
        byte[] hash = new byte[digest.getDigestSize()];
        digest.doFinal(hash, 0);
        return hash;
    }

    /**
     * @param srcData    源字符串
     * @param sm3HexData 加密后字符串
     * @Description: 判断原数据与加密数据是否一致
     * @Date: 2019/8/1 10:13
     */
    public static boolean verify(String srcData, String sm3HexData) {
        boolean flag = false;
        try {
            byte[] bytes = srcData.getBytes(ENCODING);
            byte[] newHex = hash(bytes);
            byte[] sm3Hex = ByteUtils.fromHexString(sm3HexData);
            if (Arrays.equals(newHex, sm3Hex)) flag = true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * @param srcData    源字符串
     * @param sm3HexData 加密后字符串
     * @param key        盐值
     * @Description: 判断原数据与加密数据是否一致（加盐）
     * @Date: 2019/8/1 10:13
     */
    public static boolean verifyWithKey(String srcData, String sm3HexData, byte... key) {
        boolean flag = false;
        try {
            String hmacData = encryptWithKey(srcData, key);
            if (StringUtils.equals(hmacData, sm3HexData)) flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }


    public static void main(String[] args) throws RuntimeException {
        System.out.println(encrypt("123456"));
        System.out.println(encryptWithKey("123456"));
        System.out.println(verify("123456",encrypt("123456")));
        System.out.println(verifyWithKey("123456",encryptWithKey("123456")));

    }


}
