package hq.com.aop.sm.sm2;

import hq.com.aop.sm.base.Cipher;
import hq.com.aop.sm.base.Util;
import hq.com.aop.utils.StringUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/4 16:33 星期一
 */
public class SM2Utils {
    public final static String PRIVATEKEY = "privateKey";
    public final static String PUBLICKEY = "publicKey";

    /**
     * 生成秘钥 公钥私钥
     * @return
     */
    public static Map<String,String> generateKeyPair() {
        Map<String,String> map = new HashMap<>();
        SM2 sm2 = SM2.Instance();
        AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
        ECPrivateKeyParameters ecpriv = (ECPrivateKeyParameters) key.getPrivate();
        ECPublicKeyParameters ecpub = (ECPublicKeyParameters) key.getPublic();
        BigInteger privateKey = ecpriv.getD();
        ECPoint publicKey = ecpub.getQ();
        map.put(PRIVATEKEY,Util.byteToHex(privateKey.toByteArray()));
        map.put(PUBLICKEY,Util.byteToHex(publicKey.getEncoded()));
        System.out.println("公钥: " + Util.byteToHex(publicKey.getEncoded()));
        System.out.println("私钥: " + Util.byteToHex(privateKey.toByteArray()));
        return map;
    }

    //数据加密
    public static String encrypt(byte[] publicKey, byte[] data) throws IOException {
        if (StringUtils.isEmpty(publicKey)||StringUtils.isEmpty(data)){
            return null;
        }
        byte[] source = new byte[data.length];
        System.arraycopy(data, 0, source, 0, data.length);
        Cipher cipher = new Cipher();
        SM2 sm2 = SM2.Instance();
        ECPoint userKey = sm2.ecc_curve.decodePoint(publicKey);
        ECPoint c1 = cipher.Init_enc(sm2, userKey);
        cipher.Encrypt(source);
        byte[] c3 = new byte[32];
        cipher.Dofinal(c3);
        //C1 C2 C3拼装成加密字串
        return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(source) + Util.byteToHex(c3);

    }

    //数据解密
    public static byte[] decrypt(byte[] privateKey, byte[] encryptedData) throws IOException {
        if (StringUtils.isEmpty(privateKey)||StringUtils.isEmpty(encryptedData)){
            return null;
        }
        //加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
        String data = Util.byteToHex(encryptedData);
        /***分解加密字串
         * （C1 = C1标志位2位 + C1实体部分128位 = 130）
         * （C3 = C3实体部分64位  = 64）
         * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
         */
        byte[] c1Bytes = Util.hexToByte(data.substring(0, 130));
        int c2Len = encryptedData.length - 97;
        byte[] c2 = Util.hexToByte(data.substring(130, 130 + 2 * c2Len));
        byte[] c3 = Util.hexToByte(data.substring(130 + 2 * c2Len, 194 + 2 * c2Len));
        SM2 sm2 = SM2.Instance();
        BigInteger userD = new BigInteger(1, privateKey);
        //通过C1实体字节来生成ECPoint
        ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
        Cipher cipher = new Cipher();
        cipher.Init_dec(userD, c1);
        cipher.Decrypt(c2);
        cipher.Dofinal(c3);
        //返回解密结果
        return c2;
    }

    public static void main(String[] args) throws Exception {
        //生成密钥对
        generateKeyPair();
        String plainText = "ererfeiisgod";
        byte[] sourceData = plainText.getBytes();
        //下面的秘钥可以使用generateKeyPair()生成的秘钥内容
        // 国密规范正式私钥
        String prik = "3690655E33D5EA3D9A4AE1A1ADD766FDEA045CDEAA43A9206FB8C430CEFE0D94";
        // 国密规范正式公钥
        String pubk = "04F6E0C3345AE42B51E06BF50B98834988D54EBC7460FE135A48171BC0629EAE205EEDE253A530608178A98F1E19BB737302813BA39ED3FA3C51639D7A20C7391A";

        System.out.println("加密: ");
        String cipherText = SM2Utils.encrypt(Util.hexToByte(pubk), sourceData);
        System.out.println(cipherText);
        System.out.println("解密: ");
        plainText = new String(SM2Utils.decrypt(Util.hexToByte(prik), Util.hexToByte(cipherText)));
        System.out.println(plainText);

    }
}
