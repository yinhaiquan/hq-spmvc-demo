package hq.com.email.server.send;

import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.StringUtils;
import hq.com.email.server.CommenMailHandler;
import hq.com.email.vo.EmailParams;
import hq.com.email.vo.EmailServerConfigurationParams;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @title : 邮件发送处理抽象处理器
 * @describle :
 * <p>
 *     <b>note:</b>
 *     smtp协议用于发送邮件
 *     pop3协议用户接收邮件
 * </p>
 * Create By yinhaiquan
 * @date 2017/6/12 17:50 星期一
 */
public abstract class AbstractMailSendHandler extends CommenMailHandler{

    /**
     * 系统管理员统一邮件发送配置，spring自动注入
     * 注意:
     *     若需要根据系统登录用户配置邮件发送地址，可通过查询登录用户邮件配置信息实例化JavaMailSenderImpl即可
     */
    private JavaMailSenderImpl sender;
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 手动实例化JavaMailSenderImpl
     * @param escp 邮箱服务配置信息
     * @return
     */
    public JavaMailSenderImpl configuration(EmailServerConfigurationParams escp){
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        if (StringUtils.isNotEmpty(escp)){
            sender.setHost(escp.getHost());
            sender.setPassword(escp.getPassword());
            sender.setUsername(escp.getUserName());
            sender.setJavaMailProperties(escp.getJavaMailProperties());
        }
        return sender;
    }

    /**
     * 发送普通邮件
     *
     * @param mailMessage
     */
    public void sendSimpleEmail(SimpleMailMessage mailMessage) {
        sender.send(mailMessage);
    }

    /**
     * 发送自定义消息体邮件
     */
    public void sendMimeMessage(EmailParams emailParams) throws IllegalArgumentsException {
        try {
            if (StringUtils.isNotEmpty(emailParams)){
                MimeMessage mimeMessage = formatMimeMessage(emailParams);
                if (StringUtils.isNotEmpty(mimeMessage)){
                    sender.send(mimeMessage);
                }else{
                    throw new IllegalArgumentsException("邮件信息不全");
                }
            }else{
                throw new IllegalArgumentsException("邮件信息不全");
            }
        } catch (MailException e) {
            throw new IllegalArgumentsException("发件人地址异常，请联系管理员!");
        } catch (IllegalArgumentsException e) {
            throw new IllegalArgumentsException(e);
        }

    }

    /**
     * 设置消息体
     *
     * @return
     */
    public abstract MimeMessage formatMimeMessage(EmailParams emailParams) throws IllegalArgumentsException;

    /**
     * 设置多个邮箱地址,逗号隔开
     *
     * @param address
     * @param pattern
     * @return
     */
    public String[] address(String address, String pattern) {
        if (StringUtils.isEmpty(address)||StringUtils.isEmpty(pattern)){
            return null;
        }
        return address.split(pattern);
    }

    /**
     * 设置多个邮箱地址
     *
     * @param address
     * @param pattern
     * @return
     */
    public InternetAddress[] address2(String address, String pattern) throws AddressException {
        String cc[] = address.split(pattern);
        InternetAddress[] ccadress = new InternetAddress[cc.length];
        for (int i = 0; i < cc.length; i++) {
            ccadress[i] = new InternetAddress(cc[i]);
        }
        return ccadress;
    }

    public JavaMailSenderImpl getSender() {
        return sender;
    }

    public void setSender(JavaMailSenderImpl sender) {
        this.sender = sender;
    }

    public FreeMarkerConfigurer getFreeMarkerConfigurer() {
        return freeMarkerConfigurer;
    }

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }
}
