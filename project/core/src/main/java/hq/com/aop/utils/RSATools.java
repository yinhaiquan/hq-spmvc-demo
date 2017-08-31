package hq.com.aop.utils;

import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.io.pem.PemObject;

import javax.crypto.Cipher;
import java.io.IOException;
import java.io.StringWriter;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

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
public class RSATools extends CoderUtils {
    public static final String PEM_PUBLICKEY = "PUBLIC KEY";
    public static final String PEM_PRIVATEKEY = "PRIVATE KEY";
    public static final String KEY_ALGORITHM = "RSA";
    public static final String SIGNATURE_ALGORITHEM = "SHA1WithRSA";
    /**
     * 生成私钥公钥对象
     *
     * @param keySize
     * @return
     */
    public static KeyPair generateRSAKeyPair(int keySize) {
        KeyPairGenerator generator = null;
        SecureRandom random = new SecureRandom();
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            generator = KeyPairGenerator.getInstance(KEY_ALGORITHM, "BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        generator.initialize(keySize, random);
        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }

    /**
     * 获取公钥或者私钥
     *
     * @param publicKey
     * @param privateKey
     * @return
     * @description: 1. publicKey为空 privateKey不为空 获取私钥
     *               2. publicKey不为空 privateKey为空 获取公钥
     */
    public static String convertToPemKey(RSAPublicKey publicKey,
                                         RSAPrivateKey privateKey) {
        if (publicKey == null && privateKey == null) {
            return null;
        }
        StringWriter stringWriter = new StringWriter();
        try {
            PEMWriter pemWriter = new PEMWriter(stringWriter, "BC");
            if (publicKey != null) {
                pemWriter.writeObject(new PemObject(PEM_PUBLICKEY,
                        publicKey.getEncoded()));
            } else {
                //此处产生的privatekey 的格式是 PKCS#8 的格式
                pemWriter.writeObject(new PemObject(PEM_PRIVATEKEY,
                        privateKey.getEncoded()));
            }
            pemWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringWriter.toString();
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static byte[] sign(String data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHEM);
        signature.initSign(privateKey2);
        signature.update(data.getBytes());
        return signature.sign();

    }

    /**
     * 字节转字符串
     *
     * @param bytes
     * @return
     * @description: 后台测试签名的时候 要和前台保持一致，所以需要将结果转换
     */
    private static String bytes2String(byte[] bytes) {
        StringBuilder string = new StringBuilder();
        for (byte b : bytes) {
            String hexString = Integer.toHexString(0x00FF & b);
            string.append(hexString.length() == 1 ? "0" + hexString : hexString);
        }
        return string.toString();
    }

    /**
     * 校验数字签名
     * @param data              数据
     * @param publicKey         公钥
     * @param signatureResult   签名
     * @return
     */
    public static boolean verify(String data,byte[] publicKey,byte[] signatureResult) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHEM);
            signature.initVerify(publicKey2);
            signature.update(data.getBytes());
            return signature.verify(signatureResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 格式化前端转过来的签名字符串
     *
     * @description: 前台的签名结果是将byte 中的一些 负数转换成了正数，
     *               但是后台验证的方法需要的又必须是转换之前的
     * @param data   前端传过来的签名
     * @return
     */
    public static byte[] hexStringToByteArray(String data) {
        int k = 0;
        byte[] results = new byte[data.length() / 2];
        for (int i = 0; i + 1 < data.length(); i += 2, k++) {
            results[k] = (byte) (Character.digit(data.charAt(i), 16) << 4);
            results[k] += (byte) (Character.digit(data.charAt(i + 1), 16));
        }
        return results;
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


    public static void main(String[] args) throws Exception {
        test2();
        /*测试java端签名*/
//        /*数据*/
//        String str = "123";
//        /*获取私钥公钥对象*/
//        KeyPair k = generateRSAKeyPair(1024);
//        /*前端获取公钥私钥 带-----BEGIN PRIVATE KEY-----前缀等*/
//        /*获取公钥*/
//        String publicKey = convertToPemKey((RSAPublicKey) k.getPublic(), null);
//        /*获取私钥 签名时，前端使用jsrsasign-all-min.js插件使用的私钥*/
//        String privateKey = convertToPemKey(null, (RSAPrivateKey) k.getPrivate());
//        System.out.println("publicKey__\n" + publicKey);
//        System.out.println("privateKey_\n" + privateKey);
//
//        try {
//            /*生成签名*/
//            byte[] signautreResult = sign(str, k.getPrivate().getEncoded());
//            /*字节码转字符串*/
//            String signatureStr = bytes2String(signautreResult);
//            System.out.println(signatureStr);
//            System.out.println("*****************************");
//            /*格式化签名*/
//            byte[] signatureResult2 = hexStringToByteArray(signatureStr);
//            System.out.println(bytes2String(signatureResult2));
//            System.out.println("***************privateKey**************");
//            System.out.println(CoderUtils.encryptBase64(k.getPrivate().getEncoded()));
//            System.out.println("*************publicKey****************");
//            System.out.println(CoderUtils.encryptBase64(k.getPublic().getEncoded()));
//            System.out.println("*****************************");
//            /*验证签名*/
//            boolean b = verify(str,
//                    k.getPublic().getEncoded(),
//                    signatureResult2);
//            System.out.print("iii   " + b);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /*测试秘钥加解密*/
    public static void test2() throws Exception {
/*java后台存储公钥私钥*/
        String pukey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDC9QYC4/mQFKJsoLm7j9i95We\n" +
                "r+1TruGwdWEfX5ITRIdrKDcTOBRsx7fooBBqArUCgtLru0zoRzzK564SNY/xQb11\n" +
                "oJB7zFlYUgBBkogUyIh0thn7kxJmXr5NLg/Yz0NxInQcMkxz2oYxQIpm9P3Vzqcx\n" +
                "+ljQYkrDH7rQ/8T8UQIDAQAB";
        String prkey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIML1BgLj+ZAUomy\n" +
                "gubuP2L3lZ6v7VOu4bB1YR9fkhNEh2soNxM4FGzHt+igEGoCtQKC0uu7TOhHPMrn\n" +
                "rhI1j/FBvXWgkHvMWVhSAEGSiBTIiHS2GfuTEmZevk0uD9jPQ3EidBwyTHPahjFA\n" +
                "imb0/dXOpzH6WNBiSsMfutD/xPxRAgMBAAECgYA54juo81J4jejnUaZogswU1u+L\n" +
                "zIGz+QdPfkmMJhbL0/VM69KH9rlu4zjk0+7sJI33sfmgjncGEpFTvsow2N/fNSKS\n" +
                "T+z319LubyoBI3B5aNBOvOrf0jdqZaeAqtaIafkQQ0z8bFdDisJ8j2LYmYLM1VBr\n" +
                "q9PQ6tQ3pBtt2yXswQJBAPCRQAstauJ30aoP4ZFj0Qm07P1jMPqpIioHjHpeO74i\n" +
                "/S7NGlafJirUslAoeJfigdV3G+/nv87Do+1VMfz8j50CQQCLc/PEKE/xUf6gFQUC\n" +
                "pgIUjqBCljSi43o2UwN3h7PFu3DoCeoua0L8S4TvnAzjWwP0957vYqAroADO0/hO\n" +
                "jjNFAkBepyflvfcCKFbMbPc5t2Z2RUPB5xdMaOPSWaoCM7WZ+DZaBcVJM9FWpN2M\n" +
                "AICCclLXZMgPOsYTWpLTPOQwQEdZAkA8PmNYonGmtt1hGrOHaWAMsXDaUzl6BPuO\n" +
                "7F3bDWzERy2HkIDyfLP7ekR7ntaSgYc8FJ08nUbEkeLHdxsWFKhhAkA15fsjEoeP\n" +
                "uz1BH69n832uoSMaiPrl+dAxhgXM7gUFTWiglpKUiNmm4zo4+IbieRnZo6pNqu3r\n" +
                "WrYfH+xtY5H6";

                System.out.println("用户私钥加密*************");
        String msg = "123sadfsadf";
        System.out.println("==>" + msg);
        byte[] p_k_r = encryptByPrivateKey(msg.getBytes(), prkey);
        System.out.println(encryptBase64(p_k_r));
        System.out.println("用公钥解密***************");
        String msg_ = new String(decryptByPublicKey(p_k_r, pukey));
        System.out.println("==>" + msg_);
    }

    /*测试前端签名*/
    public static void test() throws Exception {
        /*java后台存储公钥私钥*/
        String pukey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCDC9QYC4/mQFKJsoLm7j9i95We\n" +
                "r+1TruGwdWEfX5ITRIdrKDcTOBRsx7fooBBqArUCgtLru0zoRzzK564SNY/xQb11\n" +
                "oJB7zFlYUgBBkogUyIh0thn7kxJmXr5NLg/Yz0NxInQcMkxz2oYxQIpm9P3Vzqcx\n" +
                "+ljQYkrDH7rQ/8T8UQIDAQAB";
        String prkey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIML1BgLj+ZAUomy\n" +
                "gubuP2L3lZ6v7VOu4bB1YR9fkhNEh2soNxM4FGzHt+igEGoCtQKC0uu7TOhHPMrn\n" +
                "rhI1j/FBvXWgkHvMWVhSAEGSiBTIiHS2GfuTEmZevk0uD9jPQ3EidBwyTHPahjFA\n" +
                "imb0/dXOpzH6WNBiSsMfutD/xPxRAgMBAAECgYA54juo81J4jejnUaZogswU1u+L\n" +
                "zIGz+QdPfkmMJhbL0/VM69KH9rlu4zjk0+7sJI33sfmgjncGEpFTvsow2N/fNSKS\n" +
                "T+z319LubyoBI3B5aNBOvOrf0jdqZaeAqtaIafkQQ0z8bFdDisJ8j2LYmYLM1VBr\n" +
                "q9PQ6tQ3pBtt2yXswQJBAPCRQAstauJ30aoP4ZFj0Qm07P1jMPqpIioHjHpeO74i\n" +
                "/S7NGlafJirUslAoeJfigdV3G+/nv87Do+1VMfz8j50CQQCLc/PEKE/xUf6gFQUC\n" +
                "pgIUjqBCljSi43o2UwN3h7PFu3DoCeoua0L8S4TvnAzjWwP0957vYqAroADO0/hO\n" +
                "jjNFAkBepyflvfcCKFbMbPc5t2Z2RUPB5xdMaOPSWaoCM7WZ+DZaBcVJM9FWpN2M\n" +
                "AICCclLXZMgPOsYTWpLTPOQwQEdZAkA8PmNYonGmtt1hGrOHaWAMsXDaUzl6BPuO\n" +
                "7F3bDWzERy2HkIDyfLP7ekR7ntaSgYc8FJ08nUbEkeLHdxsWFKhhAkA15fsjEoeP\n" +
                "uz1BH69n832uoSMaiPrl+dAxhgXM7gUFTWiglpKUiNmm4zo4+IbieRnZo6pNqu3r\n" +
                "WrYfH+xtY5H6";
        byte[] signautreResult = sign("123", CoderUtils.decryptBase64(prkey));
        String signatureStr = bytes2String(signautreResult);
        System.out.println(signatureStr);
        System.out.println("*****************************");
        /*前端生成签名*/
        String sign = "31ac95eb2c3f3b59018d3c465ec6f656d34f2d8f9cf815498293cf1b48209814eaf7b02e74ab8bc0fcc3db3ac74c74bf66d8a3ec36ff7be293768fe80d555b8e49894dd73b1e8e1533b8d0b9f99db75515862bb9a492c82206db41e95433fe1f2706c91ff8760007b08175bf85f4e55166468b69035736e4f044f2fc836098cc";
        boolean b = verify("123",
                CoderUtils.decryptBase64(pukey),
                hexStringToByteArray(sign));
        System.out.print("iii   " + b);

    }
}
