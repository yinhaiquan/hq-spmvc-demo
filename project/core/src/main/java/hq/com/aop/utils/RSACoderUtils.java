package hq.com.aop.utils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * @title : RSA加解密工具
 * @describle :
 * <p>
 * <b>RSA:</b>
 * 这种算法1978年就出现了，它是第一个既能用于数据加密也能用于数字签名的算法。它易于理解和操作，也很流行。
 * 算法的名字以发明者的名字命名：Ron Rivest, AdiShamir 和Leonard Adleman。
 * 这种加密算法的特点主要是密钥的变化，上文我们看到DES只有一个密钥。相当于只有一把钥匙，如果这把钥匙丢了，数据也就不安全了。
 * RSA同时有两把钥匙，公钥与私钥。同时支持数字签名。数字签名的意义在于，对传输过来的数据进行校验。确保数据在传输工程中不被修改。
 * <p>
 * <b>流程分析：</b>
 * 1. 甲方构建密钥对儿，将公钥公布给乙方，将私钥保留。
 * 2. 甲方使用私钥加密数据，然后用私钥对加密后的数据签名，发送给乙方签名以及加密后的数据；乙方使用公钥、签名来验证待解密数据是否有效，如果有效使用公钥对数据解密。
 * 3. 乙方使用公钥加密数据，向甲方发送经过加密后的数据；甲方获得加密数据，通过私钥解密。
 * </p>
 * Create By yinhaiquan
 * @date 2017/5/25 17:44 星期四
 */
@Deprecated
public final class RSACoderUtils extends CoderUtils {
    //加密算法
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHEM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";
    private static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String privateKey) throws Exception {
        //解密由base64编码的私钥
        byte[] keyBytes = decryptBase64(privateKey);
        // 构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 取私钥匙对象
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHEM);
        signature.initSign(priKey);
        signature.update(data);
        return encryptBase64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @param sign      签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data, String publicKey, String sign) throws Exception {
        // 解密由base64编码的公钥
        byte[] keyBytes = decryptBase64(publicKey);
        // 构造X509EncodedKeySpec对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM 指定的加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 取公钥匙对象
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHEM);
        signature.initVerify(pubKey);
        signature.update(data);
        return signature.verify(decryptBase64(sign));
    }


    /**
     * 用公钥加密
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
        // 对公钥解密
        byte[] keyBytes = decryptBase64(publicKey);
        // 取得公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pubKey = keyFactory.generatePublic(x509EncodedKeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data);
    }

    /**
     * 用私钥加密
     *
     * @param data       加密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBase64(privateKey);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, priKey);
        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String privateKey)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBase64(privateKey);

        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, priKey);

        return cipher.doFinal(data);
    }

    /**
     * 解密<br>
     * 用公钥解密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String publicKey)
            throws Exception {
        // 对密钥解密
        byte[] keyBytes = decryptBase64(publicKey);

        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key pubKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);

        return cipher.doFinal(data);
    }


    /**
     * 获取私钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBase64(key.getEncoded());
    }

    /**
     * 获取公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBase64(key.getEncoded());
    }

    /**
     * 生成私钥公钥
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> initKey() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    public static void main(String[] args) throws Exception {
        test();
//        Map<String, Object> map = initKey();
//        String privateKey = getPrivateKey(map);
//        String publicKey = getPublicKey(map);
//        System.out.println("私钥:");
//        System.out.println(privateKey);
//        System.out.println("公钥:");
//        System.out.println(publicKey);
//        System.out.println("用户私钥加密*************");
//        String msg = "123sadfsadf";
//        System.out.println("==>" + msg);
//        byte[] p_k_r = encryptByPrivateKey(msg.getBytes(), privateKey);
//        System.out.println(encryptBase64(p_k_r));
//        System.out.println("用公钥解密***************");
//        String msg_ = new String(decryptByPublicKey(p_k_r, publicKey));
//        System.out.println("==>" + msg_);
//        System.out.println("用户公钥加密*************");
//        byte[] p_k_r2 = encryptByPublicKey(msg.getBytes(),publicKey);
//        System.out.println(encryptBase64(p_k_r2));
//        System.out.println("用私钥解密***************");
//        String msg_2 = new String(decryptByPrivateKey(p_k_r2,privateKey));
//        System.out.println("====>"+msg_2);
//
//        System.out.println("用私钥对信息生成数字签名****");
//        String pa = sign(msg.getBytes(), privateKey);
//        System.out.println(pa);
//        System.out.println("校验数字签名*****");
//        System.out.println(verify(msg.getBytes(), publicKey, pa));
//        Map<String, Object> map2 = initKey();
//        System.out.println(getPrivateKey(map2));
//        System.out.println(getPublicKey(map2));
    }

    /**
     * 测试前端jsencrypt.js插件对数据加密解密操作
     * @throws Exception
     */
    public static void test() throws Exception {
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSGUaCnoh+KyNEADIoB10CyNVatcqmgXcobcUV\n" +
                "CoyfL+9Yj1QwVnJPLJYubSVQyL8DXXvN5yZXXUNYpEoeiABhBozud4jAlEbZd5/p9yuKsCeq4kmj\n" +
                "w7IFirBLlsszxsTirdFd+Bjj1vRsoPe7DmWAIQAavThjGAjMGYCvxlov1QIDAQAB";
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJIZRoKeiH4rI0QAMigHXQLI1Vq1\n" +
                "yqaBdyhtxRUKjJ8v71iPVDBWck8sli5tJVDIvwNde83nJlddQ1ikSh6IAGEGjO53iMCURtl3n+n3\n" +
                "K4qwJ6riSaPDsgWKsEuWyzPGxOKt0V34GOPW9Gyg97sOZYAhABq9OGMYCMwZgK/GWi/VAgMBAAEC\n" +
                "gYAklifONh1AW1Uthk8JLy7fr6OeX8AsCmfou/SK3GVyx/JMXzhaEov1v9isL7EUlfEfyp1iZux3\n" +
                "HGftHtdg5fYR89pX3pFvz6RphYNniOntqIjyJvkgJUyoMZkHj0YyhHQ8ash2WmR1U5Tra4XoxgXd\n" +
                "mVG3T77dOeBchLUqrLFj0QJBAOYTEMg1jAbsg4qj06zyCrGuppSDMIKGyb88/gRio3avUK/dZHHD\n" +
                "Q9hrwo8gjHDdqgLvG7B7tlvetEHM1eQY8bsCQQCij8U2YuQsM/IQ+KObIEDASc41w9frbYjQvBkY\n" +
                "JwXSEbA0qIX9dkLXIOtzdydw8Mm2wO+Nq5BVoJpqTPldC0OvAkBMwmWh9kN+d+A3HAteMu6gcz1+\n" +
                "ZPqFQcUDeazh96rDyR/pXLrLPbNf/CvQJrLUYQ/8pJufWNbxda68GUPvMudLAkADf/rr/9cPyJKG\n" +
                "oQPMvDYIrc4dxDiq3fHbJKGZroVmj3DuwU4ZNemcfh3zn3Ye7WA6ag7+StpRraKFdWDNCwgLAkEA\n" +
                "2qDsRnoqRfm0F0isGJ4/7C/9t4CGrls8D+TMt+4tz1szoaCcCh1N9/ss1OuhlTJgTP4W8tm3YMQY\n" +
                "NxMqiiQHGA==";
        String pa = sign("123".getBytes(), privateKey);
        System.out.println(pa);
        //前端生成encrypted加密数据
//        String msg_ = "VeP0JC59ShiA+flNcxugNPJ5S91DGhdvrZpq/xcYWlmVJwi3nHypzycu3So2riHioZgNf68WJL/8cK9C5apV5g/73EPQ656ejLyeS1FQ5XVf72SxfVHJ6WZ5+7POAoxAqPzen2osq7eSJbKgHs+kpDfQOs3WFxj1JGMFk5gfNs0=";
//        System.out.println(msg_.length());
//        byte[] decodedData = decryptByPrivateKey(decryptBase64(msg_),privateKey);
//        String str = new String(decodedData);
//        System.out.println(str);
    }
}
