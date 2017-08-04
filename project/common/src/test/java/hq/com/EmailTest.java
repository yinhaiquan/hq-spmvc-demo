package hq.com;

import hq.com.email.TemplateMailHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/6/12 17:06 星期一
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-email.xml"})
public class EmailTest {

    @Resource(name = "mailHandler")
    private TemplateMailHandler javaMail;

    @Test
    public void testSendEmail() {
        Random random = new Random();
        String check = String.valueOf(random.nextInt(899999) + 100000);//随机生成6位验证码
        String message = "您的邮箱验证码是：" + check + "。有效期60秒，请及时验证！"; //邮件内容
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom("yinhaiquan@juzix.io");
        smm.setTo("1083775683@qq.com");
        smm.setSubject("test");
        smm.setText(message);
        javaMail.sendSimpleEmail(smm);
    }

    @Test
    public void testSend() {
        javaMail.sendMimeMessage();
    }
}
