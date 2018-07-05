package hq.com.aop.utils;

import org.apache.commons.codec.binary.Hex;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.math.BigInteger;
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
    public final static String encryptBase64(byte[] key) throws Exception {
        return new BASE64Encoder().encodeBuffer(key);
    }

    /**
     * base64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    public final static byte[] decryptBase64(String key) throws Exception {
        return new BASE64Decoder().decodeBuffer(key);
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * MD5加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static byte[] encryptMD5(String data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data.getBytes("utf-8"));
        return md5.digest();
    }

    /**
     * MD5加密
     * @description: 于前端md5.js生成md5加密字符一致
     * @param data 待加密数据
     * @return
     * @throws Exception
     */
    public final static String encryptMD5ToString(String data) throws Exception {
        return byteToString(encryptMD5(data));
    }

    /**
     * MD5盐值加密
     * @param data 待加密数据
     * @param salt 加盐
     * @return
     * @throws Exception
     */
    public final static String encryptMD5ToString(String data , String salt) throws Exception {
        return byteToString(encryptMD5(data+salt));
    }

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    /**
     * SHA加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public final static byte[] encryptSHA(String data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data.getBytes());
        return sha.digest();
    }


    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte) {
        String[] strDigits = { "0", "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
        int iRet = bByte;
        if (iRet < 0) {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }

    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte) {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++) {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }

    public static void main(String[] args) throws Exception {
          String str = "123sdjfh";
          byte [] datas = encryptMD5(str);
          byte [] datas2 = MessageDigest.getInstance("MD5").digest(str.getBytes());
          System.out.println(byteToString(datas));
          System.out.println(new String(Hex.encodeHex(datas2)));
//        String str = "sdf234234水电费sdf";
//        System.out.println(encryptMD5ToString(str));
//        System.out.println(encryptMD5ToString(str+"0000"));
//        String base64 = encryptBase64(str.getBytes());
//        System.out.println(base64);
//        System.out.println(new String(decryptBase64(base64),"UTF-8"));
    }
}
