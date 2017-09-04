package hq.com.aop.utils;

import hq.com.aop.sm.base.Util;
import hq.com.aop.sm.sm2.SM2Utils;
import hq.com.aop.sm.sm3.SM3Digest;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.util.Map;

/**
 * @title : SM国密加密算法
 * @describle :
 * <p>
 * <b>note:</b>
 * 包含SM2和SM3算法。
 * 1. SM2 非对称加解密 速度快于RSA1024
 * 2. SM3 类似MD5
 * </p>
 * Create By yinhaiquan
 * @date 2017/9/4 14:45 星期一
 */
public final class SMCoderUtils {

    /**
     * 基于SM3加密数据
     * @param data 待加密数据
     * @return
     */
    public final static String SM3encrypt(String data){
        if (StringUtils.isEmpty(data)){
            return null;
        }
        byte[] md = new byte[32];
        byte [] dbyte = data.getBytes();
        SM3Digest sm3 = new SM3Digest();
        sm3.update(dbyte, 0, dbyte.length);
        sm3.doFinal(md, 0);
        return new String(Hex.encode(md));
    }

    /**
     * 基于SM2生成密钥
     *
     * @description: privateKey 私钥
     *               publicKey  公钥
     * @return
     */
    public final static Map<String,String> initKeyPair(){
        return SM2Utils.generateKeyPair();
    }

    /**
     * 基于SM2数据加密
     * @param publicKey 公钥
     * @param data 待加密数据
     * @return
     * @throws IOException
     */
    public final static String encrypt(byte[] publicKey, byte[] data) throws IOException {
        return SM2Utils.encrypt(publicKey,data);
    }

    public final static String encrypt(String publicKey, String data) throws IOException {
        return encrypt(Util.hexToByte(publicKey),data.getBytes());
    }

    /**
     * 基于SM2数据解密
     * @param privateKey     私钥
     * @param encryptedData  待解密数据
     * @return
     */
    public final static String decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        return new String(SM2Utils.decrypt(privateKey, encryptedData));
    }

    public final static String decrypt(String privateKey, String encryptedData) throws IOException {
        return new String(SM2Utils.decrypt(Util.hexToByte(privateKey), Util.hexToByte(encryptedData)));
    }

    public static void main(String[] args) throws IOException {
        String str = "水电费框架zhongsdjfkl23234..][=-2=423-4o[ps/a;dkf20-3423;sdf";
        System.out.println(str.length());
        System.out.println(SM3encrypt(str));
        Map<String,String> keyPair = initKeyPair();
        String privateKey = keyPair.get(SM2Utils.PRIVATEKEY);
        String publicKey = keyPair.get(SM2Utils.PUBLICKEY);
        System.out.println("加密:");
        String result = encrypt(publicKey,str);
        System.out.println(result);
        System.out.println(result.length());
        String result2 = encrypt(publicKey,str+"12222222222222222222222223sdfasdfsadfwer.sdfa,.,123");
        System.out.println(result2);
        System.out.println(result2.length());
        System.out.println("解密:");
        System.out.println(decrypt(privateKey,result));
    }
}
