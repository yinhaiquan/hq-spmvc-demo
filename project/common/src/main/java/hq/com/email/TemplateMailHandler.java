package hq.com.email;

import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.FileParam;
import hq.com.email.vo.EmailParams;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.IOException;
import java.util.Date;

/**
 * @title : 邮件发送处理器
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/6/12 18:05 星期一
 */
public class TemplateMailHandler extends AbstractMailSendHandler {
    private final static String ENCODING = "UTF-8";

    @Override
    public MimeMessage formatMimeMessage(EmailParams emailParams) throws IllegalArgumentsException {
        MimeMessage mailM = getSender().createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mailM,true,ENCODING);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        try {
            messageHelper.setTo(emailParams.getReceiver());
            messageHelper.setSentDate(emailParams.getSentDate());
            messageHelper.setSubject(emailParams.getSubject());
            if (StringUtils.isNotEmpty(emailParams.getCc())){
                messageHelper.setCc(emailParams.getCc());
            }
            messageHelper.setValidateAddresses(true);
            if (StringUtils.isNotEmpty(emailParams.getBcc())){
                messageHelper.setBcc(emailParams.getBcc());
            }
            messageHelper.setFrom(emailParams.getFrom());
            if (emailParams.isTpl()){
                /*装载模板*/
                Template tpl = getFreeMarkerConfigurer().getConfiguration().getTemplate(emailParams.getTplName());
                /*加入map到模板中 输出对应变量*/
                System.out.println(emailParams.getData());
                emailParams.setText(FreeMarkerTemplateUtils.processTemplateIntoString(tpl,emailParams.getData()));
                System.out.println(emailParams.getText());
            }
            if (StringUtils.isNotEmpty(emailParams.getFiles())){
                for (FileParam file : emailParams.getFiles()) {
                    /*addAttachment addInline 两种附件添加方式 */
                    FileSystemResource fsr = new FileSystemResource(file.getFile());
                    /*使用MimeUtility.encodeWord 解决附件名中文乱码的问题*/
                    messageHelper.addAttachment(MimeUtility.encodeWord(file.getFileName()),fsr);
                    /*messageHelper.addInline("file222", raFileSystemResource);*/
                }
            }
            messageHelper.setText(emailParams.getText(), emailParams.isHtml());
        } catch (MessagingException e) {
            throw new IllegalArgumentsException(e);
        } catch (MalformedTemplateNameException e) {
            throw new IllegalArgumentsException(e);
        } catch (ParseException e) {
            throw new IllegalArgumentsException(e);
        } catch (TemplateNotFoundException e) {
            throw new IllegalArgumentsException(e);
        } catch (IOException e) {
            throw new IllegalArgumentsException(e);
        } catch (TemplateException e) {
            throw new IllegalArgumentsException(e);
        }
        return mailM;
    }
}
