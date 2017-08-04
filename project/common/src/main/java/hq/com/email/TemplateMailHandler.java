package hq.com.email;

import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;

/**
 * @title : 邮件发送处理器demo
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/6/12 18:05 星期一
 */
public class TemplateMailHandler extends AbstractMailSendHandler {


    @Override
    public MimeMessage formatMimeMessage() {
        MimeMessage mailM = getSender().createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailM);
        String html = "<html><head></head><body><h1>what is the fuck 测试?</h1></body></html>";
        try {
            messageHelper.setTo("kidy_yoki@sina.com");
            messageHelper.setSentDate(new Date());//设置发送日期
            messageHelper.setSubject("万万没有想到");//设置主题
            messageHelper.setCc("1083775683@qq.com");//抄送
            String bcc = "1083775683@qq.com,14123123@qq.com";
            messageHelper.setCc(address(bcc, ","));
            messageHelper.setFrom("yinhaiquan@juzix.io");
            messageHelper.setText(html, true);//true 表示启动HTML格式的邮件
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return mailM;
    }
}
