package hq.com.email;

import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.StringUtils;
import hq.com.email.vo.EmailParams;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @title : 邮件发送处理
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/6/12 17:50 星期一
 */
public abstract class AbstractMailSendHandler {

    private JavaMailSenderImpl sender;
    private SimpleMailMessage mailMessage;
    private FreeMarkerConfigurer freeMarkerConfigurer;

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

    public SimpleMailMessage getMailMessage() {
        return mailMessage;
    }

    public void setMailMessage(SimpleMailMessage mailMessage) {
        this.mailMessage = mailMessage;
    }

    public FreeMarkerConfigurer getFreeMarkerConfigurer() {
        return freeMarkerConfigurer;
    }

    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.freeMarkerConfigurer = freeMarkerConfigurer;
    }
}
