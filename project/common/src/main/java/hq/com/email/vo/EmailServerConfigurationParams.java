package hq.com.email.vo;

import hq.com.aop.utils.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Properties;

/**
 * @title : 邮件服务器收发配置信息【手动配置邮箱信息】
 * @describle :
 * <p>
 *    qq普通用户邮箱收发配置：【不建议使用qq普通邮箱配置】
 *          接收邮件服务器：imap.qq.com
 *          发送邮件服务器：smtp.qq.com
 *          账户名：您的QQ邮箱账户名（如果您是VIP邮箱，账户名需要填写完整的邮件地址）
 *          密码：您的QQ邮箱密码
 *
 *     qq企业邮箱收发配置：
 *          IMAP协议接收邮件服务器：imap.exmail.qq.com  端口143 ssl端口993
 *          pop3协议接收邮件服务器：pop.exmail.qq.com 端口110 ssl端口995
 *          SMTP协议发送邮件服务器：smtp.exmail.qq.com 端口25 ssl端口456/587
 *          账户名：您的邮箱账户名
 *          密码：您的邮箱密码
 * </p>
 * Create By yinhaiquan
 * @date 2017/8/24 11:42 星期四
 */
public class EmailServerConfigurationParams implements Serializable {
    private static final long serialVersionUID = -4818625374850598458L;
    /*邮箱服务器地址*/
    private String host;
    /*邮箱地址*/
    private String userName;
    /*密码*/
    private String password;
    /*邮箱配置属性*/
    private Properties javaMailProperties;
    /*邮箱服务器端口*/
    private String port;
    /*邮箱服务器协议*/
    private String protocal;
    /*保存邮件路径*/
    private String attachmentDir;
    private String emailDir;
    private String emailFileSuffix = ".eml";
    /*是否需要身份验证 默认true*/
    private boolean validate = true;

    public EmailServerConfigurationParams() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Properties getJavaMailProperties() {
        return javaMailProperties;
    }

    public void setJavaMailProperties(Properties javaMailProperties) {
        this.javaMailProperties = javaMailProperties;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocal() {
        return protocal;
    }

    public void setProtocal(String protocal) {
        this.protocal = protocal;
    }

    public String getAttachmentDir() {
        return attachmentDir;
    }

    public void setAttachmentDir(String attachmentDir) {
        if (!attachmentDir.endsWith(File.separator)){
            attachmentDir = attachmentDir + File.separator;
        }
        this.attachmentDir = attachmentDir;
    }

    public String getEmailDir() {
        return emailDir;
    }

    public void setEmailDir(String emailDir) {
        if (!emailDir.endsWith(File.separator)){
            emailDir = emailDir + File.separator;
        }
        this.emailDir = emailDir;
    }

    public String getEmailFileSuffix() {
        return emailFileSuffix;
    }

    public void setEmailFileSuffix(String emailFileSuffix) {
        if (!emailFileSuffix.startsWith(StringUtils.POINT)){
            emailFileSuffix = StringUtils.POINT + emailFileSuffix;
        }
        this.emailFileSuffix = emailFileSuffix;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }
}
