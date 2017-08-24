package hq.com.email.vo;

import java.io.Serializable;
import java.util.Properties;

/**
 * @title : 邮件服务配置信息【手动配置邮箱信息】
 * @describle :
 * <p>
 *    qq普通用户邮箱收发配置：【不建议使用qq普通邮箱配置】
 *          接收邮件服务器：imap.qq.com
 *          发送邮件服务器：smtp.qq.com
 *          账户名：您的QQ邮箱账户名（如果您是VIP邮箱，账户名需要填写完整的邮件地址）
 *          密码：您的QQ邮箱密码
 *
 *     qq企业邮箱收发配置：
 *          接收邮件服务器：imap.exmail.qq.com
 *          发送邮件服务器：smtp.exmail.qq.com
 *          账户名：您的邮箱账户名
 *          密码：您的邮箱密码
 * </p>
 * Create By yinhaiquan
 * @date 2017/8/24 11:42 星期四
 */
public class EmailServerConfigurationParams implements Serializable {
    private static final long serialVersionUID = -4818625374850598458L;
    /*邮箱服务地址*/
    private String host;
    /*邮箱地址*/
    private String userName;
    /*密码*/
    private String password;
    /*邮箱配置属性*/
    private Properties javaMailProperties;

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
}
