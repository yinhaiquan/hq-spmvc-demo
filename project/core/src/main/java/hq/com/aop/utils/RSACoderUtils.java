package hq.com.aop.utils;

import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.util.io.pem.PemObject;

import javax.crypto.Cipher;
import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
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
 * <b>依赖jar包：</b>
 * <dependency>
 *      <groupId>org.bouncycastle</groupId>
 *      <artifactId>bcprov-jdk16</artifactId>
 *      <version>1.46</version>
 * </dependency>
 * Create By yinhaiquan
 * @date 2017/5/25 17:44 星期四
 */
public final class RSACoderUtils extends CoderUtils {
    private static final String PEM_PUBLICKEY = "PUBLIC KEY";
    private static final String PEM_PRIVATEKEY = "PRIVATE KEY";
    private static final String KEY_ALGORITHM = "RSA";
    private static final String SIGNATURE_ALGORITHEM = "SHA1WithRSA";
    private static final String PROVIDER = "BC";
    private static final String PEM_PRK_KEY_START = "-----BEGIN PRIVATE KEY-----";
    private static final String PEM_PRK_KEY_END   = "-----END PRIVATE KEY-----";
    private static final String PEM_PUB_KEY_START = "-----BEGIN PUBLIC KEY-----";
    private static final String PEM_PUB_KEY_END   = "-----END PUBLIC KEY-----";
    private static final String RSA_PRIVATE_KEY_PEM_FILE = "g:/rsa_private_key.pem";
    private static final String RSA_PUBLIC_KEY_PEM_FILE = "g:/rsa_public_key.pem";
    private static final boolean ISSAVEPEMFILE = false;
    public static final String PKCS8_PUBLIC_KEY = "PKCS8RSAPublicKey";
    public static final String PKCS8_PRIVATE_KEY = "PKCS8RSAPrivateKey";
    public static final String PUBLIC_KEY = "RSAPublicKey";
    public static final String PRIVATE_KEY = "RSAPrivateKey";

    /**
     * 获取密钥公钥
     *
     * @description: 包括前端js签名所使用私钥，java后端使用的私钥公钥
     *               注意：前端若使用私钥公钥加密，则跟java后端使用的私钥公钥一样【前端数据加密一般不采用rsa非对称加密】
     * @return  PKCS8RSAPublicKey   前端js   公钥
     *          PKCS8RSAPrivateKey  前端js   私钥
     *          RSAPublicKey        java后端 公钥
     *          RSAPrivateKey       java后端 私钥
     *
     */
    public final static Map<String,String> getPemkey(){
        Map<String,String> keys = new HashMap<>();
        KeyPair keyPair = generateRSAKeyPair(1024);
        String publicKey = convertToPemKey((RSAPublicKey) keyPair.getPublic(), null);
        String privateKey = convertToPemKey(null, (RSAPrivateKey) keyPair.getPrivate());
        keys.put(PKCS8_PUBLIC_KEY,publicKey);
        keys.put(PKCS8_PRIVATE_KEY,privateKey);
        keys.put(PUBLIC_KEY,publicKey.replaceAll(PEM_PUB_KEY_START,"").replaceAll(PEM_PUB_KEY_END,"").trim());
        keys.put(PRIVATE_KEY,privateKey.replaceAll(PEM_PRK_KEY_START,"").replaceAll(PEM_PRK_KEY_END,"").trim());
        if (ISSAVEPEMFILE){
            saveKeyPEMFile(keyPair.getPrivate(),RSA_PRIVATE_KEY_PEM_FILE);
            saveKeyPEMFile(keyPair.getPublic(),RSA_PUBLIC_KEY_PEM_FILE);
        }
        return keys;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       待签名数据[string]
     * @param privateKey 私钥[byte]
     * @return byte
     * @throws Exception
     */
    public final static byte[] sign(String data, byte[] privateKey) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHEM);
        signature.initSign(privateKey2);
        signature.update(data.getBytes());
        return signature.sign();

    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       待签名数据[string]
     * @param privateKey 私钥[byte]
     * @return String
     * @throws Exception
     */
    public final static String signString(String data, byte[] privateKey) throws Exception {
        return bytes2String(sign(data,privateKey));
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       待签名数据[string]
     * @param privateKey 私钥[string]
     * @return byte
     * @throws Exception
     */
    public final static byte[] sign(String data, String privateKey) throws Exception {
        return sign(data,decryptBase64(privateKey));
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data       待签名数据[string]
     * @param privateKey 私钥[string]
     * @return String
     * @throws Exception
     */
    public final static String signString(String data, String privateKey) throws Exception {
        return signString(data,decryptBase64(privateKey));
    }

    /**
     * 校验数字签名
     * @param data              数据
     * @param publicKey         公钥
     * @param signatureResult   签名
     * @return
     */
    public final static boolean verify(String data,String publicKey,String signatureResult) throws Exception {
        return verify(data,decryptBase64(publicKey),hexStringToByteArray(signatureResult));
    }

    /**
     * 校验数字签名
     * @param data              数据
     * @param publicKey         公钥
     * @param signatureResult   签名
     * @return
     */
    public final static boolean verify(String data,byte[] publicKey,byte[] signatureResult) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHEM);
            signature.initVerify(publicKey2);
            signature.update(data.getBytes());
            return signature.verify(signatureResult);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 用公钥加密
     *
     * @param data      加密数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public final static byte[] encryptByPublicKey(byte[] data, String publicKey) throws Exception {
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
    public final static byte[] decryptByPrivateKey(byte[] data, String privateKey) throws Exception {
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
    public final static byte[] encryptByPrivateKey(byte[] data, String privateKey) throws Exception {
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
    public final static byte[] decryptByPublicKey(byte[] data, String publicKey) throws Exception {
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
     * 解析秘钥文件
     * @param ispkcs8  私钥是否是pkcs8格式 true是 false否且是公钥
     * @param keyFile  秘钥文件路径
     * @return
     */
    public final static String loadKeyPEMFile(boolean ispkcs8,String keyFile){
        String key = null;
        FileInputStream is = null;
        ObjectInputStream ois = null;
        try{
            is = new FileInputStream(keyFile);
            byte [] buf = new byte[is.available()];
            KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
            is.read(buf);
            EncodedKeySpec keySpec;
            if (ispkcs8){
                keySpec = new PKCS8EncodedKeySpec(buf);
            } else {
                keySpec = new X509EncodedKeySpec(buf);
            }
            key = !ispkcs8?
                    convertToPemKey((RSAPublicKey)keyFactory.generatePublic(keySpec),null):
                    convertToPemKey(null,(RSAPrivateKey)keyFactory.generatePrivate(keySpec));
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (StringUtils.isNotEmpty(is)){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return key;
    }

    public static String formatParameter(){

        return null;
    }

    /**
     * 生成秘钥文件
     * @param key 密钥
     * @param keyFilePath 密钥文件路径 rsa_private_key.pem
     *                               rsa_public_key.pem
     */
    private static void saveKeyPEMFile(Key key,String keyFilePath){
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(keyFilePath);
            os.write(key.getEncoded());
            os.flush();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (StringUtils.isNotEmpty(os)){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
    private static String convertToPemKey(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        if (publicKey == null && privateKey == null) {
            return null;
        }
        StringWriter stringWriter = new StringWriter();
        try {
            PEMWriter pemWriter = new PEMWriter(stringWriter, PROVIDER);
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
     * 格式化前端转过来的签名字符串
     *
     * @description: 前台的签名结果是将byte 中的一些 负数转换成了正数，
     *               但是后台验证的方法需要的又必须是转换之前的
     * @param data   前端传过来的签名
     * @return
     */
    private static byte[] hexStringToByteArray(String data) {
        int k = 0;
        byte[] results = new byte[data.length() / 2];
        for (int i = 0; i + 1 < data.length(); i += 2, k++) {
            results[k] = (byte) (Character.digit(data.charAt(i), 16) << 4);
            results[k] += (byte) (Character.digit(data.charAt(i + 1), 16));
        }
        return results;
    }

    /**
     * 生成私钥公钥对象
     *
     * @param keySize
     * @return
     */
    private final static KeyPair generateRSAKeyPair(int keySize) {
        KeyPairGenerator generator = null;
        SecureRandom random = new SecureRandom();
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        try {
            generator = KeyPairGenerator.getInstance(KEY_ALGORITHM, PROVIDER);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        generator.initialize(keySize, random);
        KeyPair keyPair = generator.generateKeyPair();
        return keyPair;
    }

    public static void main(String[] args) throws Exception {
//        Map<String,String> map = getPemkey();
//        System.out.println(map);
        /*解析私钥*/
//        System.out.println(loadKeyPEMFile(true,"g:/rsa_private_key.pem"));

        /*解析公钥*/
//        System.out.println(loadKeyPEMFile(false,"g:/rsa_public_key.pem"));
        test2();
        /*测试java端签名*/
//        /*数据*/
//        String str = "123";
//        /*获取私钥公钥对象*/
//        KeyPair k = generateRSAKeyPair(1024);
//        /*前端获取公钥私钥 带-----BEGIN PRIVATE KEY-----前缀等*/
//        /*获取公钥*/
//        String publicKey = convertToPemKey((RSAPublicKey) k.getPublic(), null);
//        /*获取私钥pkcs8格式 签名时，前端使用jsrsasign-all-min.js插件使用的私钥*/
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
        String pukey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWhCBkgwkpKk+VIVP8IMHM6ABp\n" +
                "CZmzfYJ+F9a//+7m+5XKrPeeisOobgR/PjSvah316YY26zZTuSDjzOvDAuf+ac2A\n" +
                "PFQyT+TU53gYdtv+aig1gLo+CgbASCZQ5X38dy4/Zjth20PDZyg4o82RcPVjOoMF\n" +
                "QOmk8hQq5kgDQ95n+QIDAQAB";
        String prkey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJaEIGSDCSkqT5Uh\n" +
                "U/wgwczoAGkJmbN9gn4X1r//7ub7lcqs956Kw6huBH8+NK9qHfXphjbrNlO5IOPM\n" +
                "68MC5/5pzYA8VDJP5NTneBh22/5qKDWAuj4KBsBIJlDlffx3Lj9mO2HbQ8NnKDij\n" +
                "zZFw9WM6gwVA6aTyFCrmSAND3mf5AgMBAAECgYAZXdkrt13C30ucQYqq8kZXJz5y\n" +
                "dVi/BEcKwy/BGfwEV6AuESqGQLKq3yfI3g35BjRYbmvdM5TrVUbyvWV6bzHzz2zL\n" +
                "0uefHuiiu0Me5ZjyLMBTS7ErjUt3Ky5EP1Mc0c8IiLkrXvKjwon2+FlDKJl18Dms\n" +
                "Tn4A7V/k6IMeqxNZtQJBAMRtR4OAQwCpnvRgIFE6VEqErb6T2kIpelx9nFR64R3c\n" +
                "FBo3rv+0b0n42mQcI4hBzQsNxEhTxqrKSxAQaQ6UnJMCQQDEKk3SILj9yi4v9lWh\n" +
                "Sg6tJskkk2PLChC2VvhZHzHy/nFMw1TYaE3CVYHXwglAIy0C5VpexQaNi4H081Dq\n" +
                "QszDAkEAj7D5baM4YJW06EQhoAoxe0nP5+g0881v65Uf9VTmtXc3ZW5yoDAYcV6Q\n" +
                "NEe5XoX0Py/U9KwEWxAdFSVMFRR17QJAJ1uJg5hXJWxUOgFFivfN3AXFI5aC1jDt\n" +
                "y3fFmjP9FJDicJFcS5MZztzTEVP4AStNk6Aqsor7Vpjf+SJ8YJQIewJAQaI5skTY\n" +
                "M1EuMdZvGw2VLyjhhEUUeupXhzE7J44OCl/mliZ3xdP1Ye/5xBvGyFFchdqhaFU2\n" +
                "Qh2tbwOorH9BoQ==";
        System.out.println(encryptBase64(encryptByPublicKey("123是可点击sdfasd12321".getBytes(),pukey)));
        System.out.println("用户私钥加密*************");
        String msg = "123呵呵123";
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
        String pukey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCWhCBkgwkpKk+VIVP8IMHM6ABp\n" +
                "CZmzfYJ+F9a//+7m+5XKrPeeisOobgR/PjSvah316YY26zZTuSDjzOvDAuf+ac2A\n" +
                "PFQyT+TU53gYdtv+aig1gLo+CgbASCZQ5X38dy4/Zjth20PDZyg4o82RcPVjOoMF\n" +
                "QOmk8hQq5kgDQ95n+QIDAQAB";
        String prkey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJaEIGSDCSkqT5Uh\n" +
                "U/wgwczoAGkJmbN9gn4X1r//7ub7lcqs956Kw6huBH8+NK9qHfXphjbrNlO5IOPM\n" +
                "68MC5/5pzYA8VDJP5NTneBh22/5qKDWAuj4KBsBIJlDlffx3Lj9mO2HbQ8NnKDij\n" +
                "zZFw9WM6gwVA6aTyFCrmSAND3mf5AgMBAAECgYAZXdkrt13C30ucQYqq8kZXJz5y\n" +
                "dVi/BEcKwy/BGfwEV6AuESqGQLKq3yfI3g35BjRYbmvdM5TrVUbyvWV6bzHzz2zL\n" +
                "0uefHuiiu0Me5ZjyLMBTS7ErjUt3Ky5EP1Mc0c8IiLkrXvKjwon2+FlDKJl18Dms\n" +
                "Tn4A7V/k6IMeqxNZtQJBAMRtR4OAQwCpnvRgIFE6VEqErb6T2kIpelx9nFR64R3c\n" +
                "FBo3rv+0b0n42mQcI4hBzQsNxEhTxqrKSxAQaQ6UnJMCQQDEKk3SILj9yi4v9lWh\n" +
                "Sg6tJskkk2PLChC2VvhZHzHy/nFMw1TYaE3CVYHXwglAIy0C5VpexQaNi4H081Dq\n" +
                "QszDAkEAj7D5baM4YJW06EQhoAoxe0nP5+g0881v65Uf9VTmtXc3ZW5yoDAYcV6Q\n" +
                "NEe5XoX0Py/U9KwEWxAdFSVMFRR17QJAJ1uJg5hXJWxUOgFFivfN3AXFI5aC1jDt\n" +
                "y3fFmjP9FJDicJFcS5MZztzTEVP4AStNk6Aqsor7Vpjf+SJ8YJQIewJAQaI5skTY\n" +
                "M1EuMdZvGw2VLyjhhEUUeupXhzE7J44OCl/mliZ3xdP1Ye/5xBvGyFFchdqhaFU2\n" +
                "Qh2tbwOorH9BoQ==";
        byte[] signautreResult = sign("123", CoderUtils.decryptBase64(prkey));
//        String signatureStr = bytes2String(signautreResult);
        String signatureStr = signString("123sdfzh中国",prkey);
        System.out.println(signatureStr);
        System.out.println("*****************************");
        /*前端生成签名*/
        String sign = "13d373716b61f1ebac29c9cd7f1a4f8cd6ecf4d8d89c411efc45e671967f68414d2e2198bbad23990310e9596bc495658c18fa4a4b62b676e5db568b419c4e73580bd8d62c5fc947d6d101823f9ba762ae1ae284196a7a0276d7008de66cafafde34d6f3564a186e41e45712ed42c8ac49f97c2fafc278295d04c50c145a34c2";
        boolean b = verify("123",pukey,signatureStr);
        System.out.print("iii   " + b);

    }
}
