package hq.com.aop.utils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 
 ***************************************************************  
 * <b>项目名称：</b>吉美营养线<br>
 * <b>程序名称：</b>图片压缩处理工具类<br>
 * <b>日期：</b>2015年5月18日-上午10:01:36<br>
 * <b>作者：</b>yinHaiquan<br>
 * <b>模块： </b>主模块<br>
 * <b>描述： </b><br>
 * <b>备注： </b><br>
 * ------------------------------------------------------------ 
 * 修改历史 
 * ------------------------------------------------------------ 
 * 序号               日期                       修改人                   修改原因 
 * ------------------------------------------------------------  
 *  
 * 修改备注： 
 * <p>@version</p>
 * <p></p>
 ***************************************************************
 */
public class ImageCompressTool {
	 private Image image;
	 //压缩后保存文件名(全路径)
	 private String fileName;
     //压缩宽度
	 private int width;
	 //压缩长度
	 private int heigth;
	 //是否等比缩放标记(默认为等比缩放)
     private boolean proportion = true;
     
     /**
      * 构造方法
      * 
      * @param file                源文件     
      * @param fileName            保存文件路径(包括文件名)
      * @param tag                 等比缩放标记     true-等比缩放  false-原比例缩放
      * @throws IOException
      */
     public ImageCompressTool(InputStream file,String fileName,boolean tag) throws IOException{
    	 this.image = ImageIO.read(file);
    	 this.fileName = fileName;
    	 this.proportion = tag;
     }
    
     /**
      * 图片压缩比例处理方法
      * 
      * @param w                          压缩宽度
      * @param h                          压缩长度
      * @throws ImageFormatException
      * @throws IOException
      */
     public void imageCompress(int w,int h) throws ImageFormatException, IOException{
    	// 判断是否是等比缩放 
         if (this.proportion) { 
                 // 为等比缩放计算输出的图片宽度及高度 
                 double rate1 = ((double) image.getWidth(null)) / (double) w + 0.1; 
                 double rate2 = ((double) image.getHeight(null)) / (double) h + 0.1; 
                 // 根据缩放比率大的进行缩放控制 
                 double rate = rate1 > rate2 ? rate1 : rate2; 
                 this.width = (int) (((double) image.getWidth(null)) / rate); 
                 this.heigth = (int) (((double) image.getHeight(null)) / rate); 
         } else { 
        	     this.width = image.getWidth(null); // 输出的图片宽度 
        	     this.heigth = image.getHeight(null); // 输出的图片高度 
         }
         resize();
     }
     
     /**
      * 压缩图片生成方法
      * 
      * @throws ImageFormatException
      * @throws IOException
      */
     private void resize() throws ImageFormatException, IOException{
    	 BufferedImage tag = new BufferedImage((int) this.width, (int) this.heigth, BufferedImage.TYPE_INT_RGB); 
         /*
          * Image.SCALE_SMOOTH 的缩略算法 生成缩略图片的平滑度的优先级比速度高 生成的图片质量比较好 但速度慢
          */ 
         tag.getGraphics().drawImage(image.getScaledInstance(width, heigth, Image.SCALE_SMOOTH), 0, 0, null);
         FileOutputStream out = new FileOutputStream(this.fileName);
         // JPEGImageEncoder可适用于其他图片类型的转换 
         JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out); 
         encoder.encode(tag); 
         out.close();
     }
     
     // main测试 
     public static void main(String[] arg) { 
    	    ImageCompressTool mypic;
			try {
				mypic = new ImageCompressTool(new FileInputStream(new File("e:\\123.png")),"e:/f2.jpg",true);
	            mypic.imageCompress(400,300);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

     } 
}
