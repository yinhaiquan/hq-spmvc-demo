package hq.com.email.vo;

import hq.com.aop.vo.FileParam;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * @title : 邮件对象封装
 * @describle : 邮件发送参数封装
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/15 11:38 星期二
 */
public class EmailParams implements Serializable{
    private static final long serialVersionUID = -4123966721335358256L;

    /*接收者列表 [必填]*/
    private String [] receiver;
    /*发送者 [必填]*/
    private String from;
    /*抄送者列表*/
    private String [] cc;
    /*暗抄送者列表 注意：接收者、抄送者中不会出现暗抄送者信息，只有暗抄送者自己可见*/
    private String [] bcc;
    /*邮件主题 [必填]*/
    private String subject;
    /*发送日期*/
    private Date sentDate;
    /*邮件内容 [必填]*/
    private String text;
    /*是否启用html格式 [必填]*/
    private boolean isHtml = false;
    /*附件列表*/
    private FileParam [] files;
    /*是否启用freeMarker模板引擎 [必填]*/
    private boolean isTpl = false;
    /*模板名称*/
    private String tplName;
    /*模板中导入数据*/
    private Map<String,Object> data;

    public EmailParams() {
    }



    public String[] getReceiver() {
        return receiver;
    }

    public void setReceiver(String[] receiver) {
        this.receiver = receiver;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String[] getCc() {
        return cc;
    }

    public void setCc(String[] cc) {
        this.cc = cc;
    }

    public String[] getBcc() {
        return bcc;
    }

    public void setBcc(String[] bcc) {
        this.bcc = bcc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public FileParam[] getFiles() {
        return files;
    }

    public void setFiles(FileParam[] files) {
        this.files = files;
    }

    public boolean isTpl() {
        return isTpl;
    }

    public void setTpl(boolean tpl) {
        isTpl = tpl;
    }

    public String getTplName() {
        return tplName;
    }

    public void setTplName(String tplName) {
        this.tplName = tplName;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EmailParams{" +
                ", receiver=" + Arrays.toString(receiver) +
                ", from='" + from + '\'' +
                ", cc=" + Arrays.toString(cc) +
                ", bcc=" + Arrays.toString(bcc) +
                ", subject='" + subject + '\'' +
                ", sentDate=" + sentDate +
                ", text='" + text + '\'' +
                ", isHtml=" + isHtml +
                ", files=" + Arrays.toString(files) +
                ", isTpl=" + isTpl +
                ", tplName='" + tplName + '\'' +
                ", data=" + data +
                '}';
    }
}
