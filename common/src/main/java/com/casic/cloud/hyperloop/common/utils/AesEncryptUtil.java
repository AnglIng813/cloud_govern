package com.casic.cloud.hyperloop.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.Charsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * @Description:  AES/CBC/PKCS5Padding，AES/CBC/NoPadding加密解密工具类
 * @Author: LDC
 * @Date: 2019/9/17 17:02
*/
public class AesEncryptUtil {
    private static final String UTF8 = "UTF-8";
    private static final String AES = "AES";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String AES_CBC_NO_PADDING = "AES/CBC/NoPadding";
    // 偏移量
    private static String IV_PARAMETER = "0102030405060708";

    //算法/模式/补码方式
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";
    // 密匙key
    private static String DEFAULT_KEY = "HT706HT706706706";


    /**
     * @Description:  格式化秘钥为128bit
     *                 JDK只支持AES-128加密，也就是密钥长度必须是128bit；参数为密钥key，
     *                 key的长度小于16字符时用"0"补充，key长度大于16字符时截取前16位
     * @Author: LDC
     * @Date: 2019/9/17 16:12
    */
    private static SecretKeySpec create128BitsKey(String key) {
        if (key == null) {
            key = "";
        }
        byte[] data = null;
        StringBuffer buffer = new StringBuffer(16);
        buffer.append(key);
        //小于16后面补0
        while (buffer.length() < 16) {
            buffer.append("0");
        }
        //大于16，截取前16个字符
        if (buffer.length() > 16) {
            buffer.setLength(16);
        }
        try {
            data = buffer.toString().getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(data, AES);
    }

    /**
     * @Description:  格式化位移量
     *                  创建128位的偏移量，iv的长度小于16时后面补0，大于16，截取前16个字符;
     * @Author: LDC
     * @Date: 2019/9/17 16:14
    */
    private static IvParameterSpec create128BitsIV(String iv) {
        if (iv == null) {
            iv = "";
        }
        byte[] data = null;
        StringBuffer buffer = new StringBuffer(16);
        buffer.append(iv);
        while (buffer.length() < 16) {
            buffer.append("0");
        }
        if (buffer.length() > 16) {
            buffer.setLength(16);
        }
        try {
            data = buffer.toString().getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new IvParameterSpec(data);
    }


    /**
     * @Description: AES/CBC/PKCS5Padding 加密
     *                  填充方式为Pkcs5Padding时，最后一个块需要填充χ个字节，填充的值就是χ，也就是填充内容由JDK确定
     * @Author: LDC
     * @Date: 2019/9/17 16:32
    */
    private static byte[]  aesCbcPkcs5PaddingEncrypt(byte[] srcContent , String password, String iv) {

        SecretKeySpec key = create128BitsKey(password);
        IvParameterSpec ivParameterSpec = create128BitsIV(iv);
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            byte[] encryptedContent = cipher.doFinal(srcContent);
            return encryptedContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @Description: AES/CBC/PKCS5Padding 加密
     *                  填充方式为Pkcs5Padding时，最后一个块需要填充χ个字节，填充的值就是χ，也就是填充内容由JDK确定
     * @Author: LDC
     * @Date: 2019/9/17 16:32
     */
    public static String aesCbcPkcs5PaddingEncrypt(String content) {

        byte[] encrypt = AesEncryptUtil.aesCbcPkcs5PaddingEncrypt(content.getBytes(), AesEncryptUtil.DEFAULT_KEY, AesEncryptUtil.IV_PARAMETER);
        return Base64.encodeBase64String(encrypt);
    }

    /**
     * @Description: AES/CBC/PKCS5Padding 解密
     * @Author: LDC
     * @Date: 2019/9/17 16:32
     */
    private static byte[] aesCbcPkcs5PaddingDecrypt(byte[] encryptedContent, String password, String iv) {

        if(encryptedContent.length<=0)
            throw new RuntimeException("密文不能为空");

        SecretKeySpec key = create128BitsKey(password);
        IvParameterSpec ivParameterSpec = create128BitsIV(iv);
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            byte[] decryptedContent = cipher.doFinal(encryptedContent);
            return decryptedContent;
        } catch (Exception e) {

            throw new RuntimeException("密文格式不正确");
        }
    }

    /**
     * @Description: AES/CBC/PKCS5Padding 解密
     * @Author: LDC
     * @Date: 2019/9/17 16:32
     */
    public static String aesCbcPkcs5PaddingDecrypt(String content) {

        byte[] encryptedContent = Base64.decodeBase64(content);
        byte[] decryptedContent = AesEncryptUtil.aesCbcPkcs5PaddingDecrypt(encryptedContent, AesEncryptUtil.DEFAULT_KEY, AesEncryptUtil.IV_PARAMETER);

        return new String(decryptedContent);
    }


    /**
     * @Description: AES/CBC/NoPadding 加密 
     *                  填充方式为NoPadding时，最后一个块的填充内容由程序员确定，通常为0.
     *                  AES/CBC/NoPadding加密的明文长度必须是16的整数倍，明文长度不满足16时，程序员要扩充到16的整数倍
     * @Author: LDC
     * @Date: 2019/9/17 16:39
     * @version: V1.0
    */
    private static byte[] aesCbcNoPaddingEncrypt(byte[] sSrc, String aesKey, String aesIV) {
        //加密的数据长度不是16的整数倍时，原始数据后面补0，直到长度满足16的整数倍
        int len = sSrc.length;
        //计算补0后的长度
        while (len % 16 != 0) len++;
        byte[] result = new byte[len];
        //在最后补0
        for (int i = 0; i < len; ++i) {
            if (i < sSrc.length) {
                result[i] = sSrc[i];
            } else {
                //填充字符'a'
                //result[i] = 'a';
                result[i] = 0;
            }
        }
        SecretKeySpec skeySpec = create128BitsKey(aesKey);
        //使用CBC模式，需要一个初始向量iv，可增加加密算法的强度
        IvParameterSpec iv = create128BitsIV(aesIV);
        Cipher cipher = null;
        try {
            //算法/模式/补码方式
            cipher = Cipher.getInstance(AES_CBC_NO_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] encrypted = null;
        try {
            encrypted = cipher.doFinal(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypted;
    }

    /**
     * @Description: AES/CBC/NoPadding 解密
     * @Author: LDC
     * @Date: 2019/9/17 16:39
     * @version: V1.0
     */
    private static byte[] aesCbcNoPaddingDecrypt(byte[] sSrc, String aesKey, String aesIV) {

        if(sSrc.length<=0)
            throw new RuntimeException("密文不能为空");

        SecretKeySpec skeySpec = create128BitsKey(aesKey);
        IvParameterSpec iv = create128BitsIV(aesIV);
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_NO_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] decryptContent = cipher.doFinal(sSrc);
            return decryptContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES解密String 此方法使用AES-128-ECB加密模式，key需要为16位
     * @param content 加密密文
     * @return 解密明文
     */
    public static String aesEcbDecode(String content){
        try {
            //根据key生成AES密钥
            SecretKeySpec skey = create128BitsKey(DEFAULT_KEY);
            //根据指定算法生成密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化密码器，第一个参数为加密|解密操作，第二个参数为生成的AES密钥
            cipher.init(Cipher.DECRYPT_MODE,skey);
            //把密文字符串转回密文字节数组
            byte[] encode_content = Base64.decodeBase64(content);
            //密码器解密数据
            byte[] byte_content = cipher.doFinal(encode_content);
            //将解密后的数据转为字符串返回
            return new String(byte_content, Charsets.UTF_8);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密 此方法使用AES-128-ECB加密模式，key需要为16位
     * @param content 加密内容
     * @return 加密密文
     */
    public static String aesEcbEncode(String content){
        try {
            SecretKeySpec skey = create128BitsKey(DEFAULT_KEY);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE,skey);
            byte[] byte_content = content.getBytes(Charsets.UTF_8);
            byte[] encode_content = cipher.doFinal(byte_content);
            return Base64.encodeBase64String(encode_content);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    /**
     * AES解密byte[] 此方法使用AES-128-ECB加密模式，key需要为16位
     * @param content 加密密文
     * @return 解密明文
     */
    public static byte[] aesEcbDecodeByte(byte[] content){
        try {
            //根据key生成AES密钥
            SecretKeySpec skey = create128BitsKey(DEFAULT_KEY);
            //根据指定算法生成密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            //初始化密码器，第一个参数为加密|解密操作，第二个参数为生成的AES密钥
            cipher.init(Cipher.DECRYPT_MODE,skey);
            //把密文字符串转回密文字节数组
            byte[] encode_content = Base64.decodeBase64(content);
            //密码器解密数据
            return  cipher.doFinal(encode_content);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 十六进制字符串转为数组
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr){
       if(hexStr.length() < 1){
           return  null;
       }
       byte[] result = new byte[hexStr.length()/2];
       for(int i = 0;i<hexStr.length()/2;i++){
           int high = Integer.parseInt(hexStr.substring(i*2,i*2+1),16);
           int low = Integer.parseInt(hexStr.substring(i*2+1,i*2+2),16);
           result[i] = (byte) (high*16 + low);
       }
       return result;
    }

    public static void main(String[] args) {

        String data = "{pageNum: 1, pageSize: 10, userId: 2, pageTag: 1}";
        //加密
        String s = AesEncryptUtil.aesEcbEncode(data);
//        System.out.println(s);
        //解密
//        System.out.println(AesEncryptUtil.aesCbcPkcs5PaddingDecrypt(s));

        System.out.println(AesEncryptUtil.aesEcbDecode("/spGQQuWxJGa7lcXQ8h4A5yaowPnpszx0rJv3OiCxzduUkinBN4U4RxX8wEMEUMf5ERk6nHgvRLMCVLLgS7GdZnT8JmxMt9B8O3mZR/XJSnLJ+N2q5Q/zRlKk5KMBqB4hKBqYjlnYNoiC1Djl0QnhCCSrIaDCsXkZ50DaDY3ob28tHqFrd8lJ9VX6Az9K2xy767BdrEN5qW9yCIUMUvt/QnUP3HSM8Rhruf6Gmp4BAU="));
//        System.out.println(AesEncryptUtil.aesEcbDecode("Fh1798vVaG6532H0E+LdTjppJoNEfZWZNu4Wvt2FWje1NVpTBuWaWviWN0AC0PTJ3cPYplDxLxZofsIEJhff0rRQjzo+LlxquFXNUxisoBj+H3pPLgeKryg7P0g3Vxie2lDCRCFhL8O9tg1j1PrUJL3TGsaxw4gz9xHw/gPXmDGLIEXdb+csBq3XLo8JwyNethjdbB6tPo04/WWQupQlFfYuaMJ2tQKSENSZt9U6wEjVruIS2zCwDafbiYizYEsGELoQcNzHCYbpXjxevnp1iNPtiJVtzgvSkjjXRvlV3NnOtW7jp7UHgX2zpUrcbElfB6O91fCvfsbGBov6cD0Av44V5uv7dzjuR7nMsXwzJbPvdha+2JA6l+bwjx1Uo1En"));
//        System.out.println(s);
    }

}
