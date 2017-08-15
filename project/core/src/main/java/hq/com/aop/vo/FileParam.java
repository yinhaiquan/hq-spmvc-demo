package hq.com.aop.vo;

import java.io.File;
import java.io.Serializable;

/**
 * @title : 文件公共实体类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/15 11:41 星期二
 */
public class FileParam implements Serializable {
    private static final long serialVersionUID = -309866423831072078L;
    /*文件名*/
    private String fileName;
    /*文件路径*/
    private String fileLocation;
    /*是否是图片*/
    private boolean isImage;
    /*文件*/
    private File file;
    /*是否是base64图片*/
    private boolean isBase64Image;
    /*图片base64内容*/
    private String fileBase64Image;

    public FileParam() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public boolean isImage() {
        return isImage;
    }

    public void setImage(boolean image) {
        isImage = image;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isBase64Image() {
        return isBase64Image;
    }

    public void setBase64Image(boolean base64Image) {
        isBase64Image = base64Image;
    }

    public String getFileBase64Image() {
        return fileBase64Image;
    }

    public void setFileBase64Image(String fileBase64Image) {
        this.fileBase64Image = fileBase64Image;
    }
}
