package hq.com.aop.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

/**
 * @title : 基础加密解密工具类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/25 17:27 星期四
 */
public class CoderUtils {
    public static final String KEY_SHA = "SHA";
    public static final String KEY_MD5 = "MD5";

    /**
     * base64加密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBase64(byte[] key) throws Exception {
        return new BASE64Encoder().encodeBuffer(key);
    }

    /**
     * base64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBase64(String key) throws Exception {
        return new BASE64Decoder().decodeBuffer(key);
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    public static void main(String[] args) throws Exception {
        String str = "sdf234234水电费sdf";
//        String base64 = encryptBase64(str.getBytes());
//        System.out.println(base64);
//        System.out.println(new String(decryptBase64(base64),"UTF-8"));
    }
}
