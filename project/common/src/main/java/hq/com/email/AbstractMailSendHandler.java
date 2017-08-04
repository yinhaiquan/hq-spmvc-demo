package hq.com.email;

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
    public void sendMimeMessage() {
        sender.send(formatMimeMessage());
    }

    /**
     * 设置消息体
     *
     * @return
     */
    public abstract MimeMessage formatMimeMessage();

    /**
     * 设置多个邮箱地址
     *
     * @param address
     * @param pattern
     * @return
     */
    public String[] address(String address, String pattern) {
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
