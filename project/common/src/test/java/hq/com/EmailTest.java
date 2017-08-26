package hq.com;

import hq.com.aop.concurrency.AsyncEvent;
import hq.com.aop.concurrency.AsyncHandlerListener;
import hq.com.aop.concurrency.AsyncObject;
import hq.com.aop.concurrency.impl.AsyncEventSourceImpl;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.DateUtils;
import hq.com.aop.vo.FileParam;
import hq.com.email.server.send.AbstractMailSendHandler;
import hq.com.email.server.send.TemplateMailSendHandler;
import hq.com.email.vo.EmailParams;
import hq.com.email.vo.EmailServerConfigurationParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

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
    private TemplateMailSendHandler javaMail;

    @Resource(name = "asyncEventSource")
    private AsyncEventSourceImpl asyncEventSource;

    @Resource(name = "mailHandler")
    private AbstractMailSendHandler mailHandler;

    @Test
    public void testSendEmail() {
        EmailServerConfigurationParams escp = new EmailServerConfigurationParams();
        escp.setHost("smtp.exmail.qq.com");
        escp.setUserName("yinhaiquan@juzix.io");
        escp.setPassword("yhq123456");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth",true);
        javaMailProperties.put("mail.debug",true);
        escp.setJavaMailProperties(javaMailProperties);
        mailHandler.setSender(mailHandler.configuration(escp));
        System.out.println(mailHandler.getSender().getHost());
        Random random = new Random();
        String check = String.valueOf(random.nextInt(899999) + 100000);//随机生成6位验证码
        String message = "您的邮箱验证码是：" + check + "。有效期60秒，请及时验证！"; //邮件内容
        SimpleMailMessage smm = new SimpleMailMessage();
        smm.setFrom(mailHandler.getSender().getUsername());
        smm.setTo("1083775683@qq.com");
        smm.setSubject("test");
        smm.setText(message);
        mailHandler.sendSimpleEmail(smm);
    }

    @Test
    public void testSend() throws IllegalArgumentsException {
        javaMail.sendMimeMessage(null);
    }

    /**
     * 借助java消息机制，创建消息队列，提供并发量
     * @throws InterruptedException
     */
    @Test
    public void testcurrentemail() throws InterruptedException {
        AsyncHandlerListener asyncHandlerListener = new AsyncHandlerListener() {
            @Override
            public void eventHandler(AsyncEvent asyncEvent) throws IllegalArgumentsException {
                mailHandler.sendMimeMessage((EmailParams) asyncEvent.getParams().get("data"));
            }
        };
        FileParam fileParam = new FileParam();
        fileParam.setFileName("图片");
        fileParam.setFile(new File("C:\\Users\\kidy\\Pictures\\Feedback\\{C4982F87-D49C-46D9-AF06-C117412CA545}\\Capture001.png"));
        AsyncObject asyncObject_1 = new AsyncObject();
        asyncObject_1.setAsyncHandlerListener(asyncHandlerListener);
        Map<String, Object> param_1 = new HashMap<>();
        EmailParams emailParams = new EmailParams();
        emailParams.setText("thanks");
        emailParams.setFrom(mailHandler.getSender().getUsername());
        emailParams.setReceiver(new String[]{"1083775683@qq.com"});
        emailParams.setSubject("test");
        emailParams.setSentDate(DateUtils.stringToDate("2018-8-8 00:00:00",DateUtils.YYYY_MM_DD_HH_MM_SS));
        emailParams.setFiles(new FileParam[]{fileParam,fileParam,fileParam});
        param_1.put("data", emailParams);
        asyncObject_1.setParams(param_1);

        AsyncObject asyncObject_2 = new AsyncObject();
        asyncObject_2.setAsyncHandlerListener(asyncHandlerListener);
        Map<String, Object> param_2 = new HashMap<>();
        Map map = new HashMap();
        map.put("userName", "test");
        map.put("sendTime", DateUtils.dateToString(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS));
        emailParams.setData(map);
        emailParams.setTpl(true);
        emailParams.setHtml(true);
        emailParams.setTplName("sendTemplate.ftl");
        param_2.put("data", emailParams);
        asyncObject_2.setParams(param_2);

        asyncEventSource.addListener(asyncObject_1);
        asyncEventSource.addListener(asyncObject_2);
        asyncEventSource.signalAll();
        Thread.sleep(30 * 1000);
    }
}
