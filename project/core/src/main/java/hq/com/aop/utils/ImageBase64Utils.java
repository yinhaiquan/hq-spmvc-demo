package hq.com.aop.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * @title : 图片Base64格式互转工具类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/27 14:19 星期四
 */
public class ImageBase64Utils {

    /**
     * 图片转化成base64字符串
     *
     * <p>
     *     <b>note:</b>
     *     将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     * </p>
     * @param obj 待处理图片 可为图片物理地址，或者File
     * @return
     */
    public static final String imageToBase64Str(Object obj){
        File image = null;
        InputStream in = null;
        byte[] data = null;
        try {
            if (StringUtils.isNotEmpty(obj)){
                if (obj instanceof String){
                    image = new File((String) obj);
                } else if (obj instanceof File){
                    image = (File) obj;
                } else {
                    throw new Exception("传参格式错误!");
                }
                in = new FileInputStream(image);
                data = new byte[in.available()];
                in.read(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (StringUtils.isNotEmpty(in)){
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return StringUtils.isNotEmpty(data)?encoder.encode(data):null;
    }

    /**
     * base64字符串转化成图片
     *
     * <p>
     *     <b>note:</b>
     *     对字节数组字符串进行Base64解码并生成图片
     * </p>
     * @param imageBase64Str 图片Base64位加密字符串
     * @param newImageUrl 图片生成地址
     * @return
     */
    public static final boolean base64StrToImage(String imageBase64Str,String newImageUrl){
        if (StringUtils.isEmpty(imageBase64Str)){
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        OutputStream out = null;
        try {
            byte [] data = decoder.decodeBuffer(imageBase64Str);
            for (int i =0 ; i<data.length;i++){
                if (data[i]<0){
                    data[i]+=256;
                }
            }
            out = new FileOutputStream(newImageUrl);
            out.write(data);
            out.flush();
        } catch (Exception e){
                return false;
        } finally {
            if (StringUtils.isNotEmpty(out)){
                try {
                    out.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {

        System.out.println(ImageBase64Utils.imageToBase64Str(new File("C:\\Users\\kidy\\Desktop\\111\\111.png")));
    }
}
