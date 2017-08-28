package hq.com.email.server.receiver;

import hq.com.aop.exception.IllegalArgumentsException;
import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.FileParam;
import hq.com.email.vo.EmailAttachMentParams;
import hq.com.email.vo.EmailParams;
import hq.com.email.vo.EmailServerConfigurationParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @title : 邮件接收服务处理器
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/25 14:24 星期五
 */
public class TemplateMailReciverHandler extends AbstractMailReciverHandler{
    private static Logger log = LoggerFactory.getLogger(TemplateMailReciverHandler.class);

    public TemplateMailReciverHandler(EmailServerConfigurationParams escp) {
        super(escp);
    }

    /**
     * 获取邮件详情
     *
     * @return
     */
    @Override
    public EmailAttachMentParams formartEamilInfo() throws MessagingException, UnsupportedEncodingException {
        EmailAttachMentParams ep = new EmailAttachMentParams();
        ep.setHtml(true);
        ep.setEmailServerId(((MimeMessage)currentMessage).getMessageID());
        ep.setSentDate(currentMessage.getSentDate());
        ep.setSubject(getSubject());
        ep.setFrom(getFrom());
        ep.setReceiver(getAddress(Message.RecipientType.TO));
        ep.setCc(getAddress(Message.RecipientType.CC));
        ep.setBcc(getAddress(Message.RecipientType.BCC));
        return ep;
    }

    /**
     * 保存邮件源文件eml路径
     */
    @Override
    public void saveMLFile(Message message) throws MessagingException, IOException {
        //将邮件的ID中尖括号中的部分做为邮件的文件名
        String oriFileName =getInfoBetweenBrackets(((MimeMessage)currentMessage).getMessageID());
        //设置文件后缀名。若是附件则设法取得其文件后缀名作为将要保存文件的后缀名，
        String emlName = oriFileName;
        String fileNameWidthExtension = escp.getEmailDir()+oriFileName+escp.getEmailFileSuffix();
        File storeFile = new File(fileNameWidthExtension);
        for (int i=0;storeFile.exists();i++){
            emlName = oriFileName+i;
            fileNameWidthExtension = escp.getEmailDir()+emlName+escp.getEmailFileSuffix();
            storeFile = new File(fileNameWidthExtension);
        }
        currentEmailFileName = emlName;

        return;
        //不用存储eml文件，可登陆自己邮件下载eml文件，此处第三方下载速度很慢，不建议在第三方下载eml
//        // 将邮件消息的内容写入ByteArrayOutputStream流中
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        message.writeTo(baos);
//        // 读取邮件消息流中的数据
//        StringReader in = new StringReader(baos.toString());
//        // 存储到文件
//        saveFile(fileNameWidthExtension, in);
//        log.info("邮件消息的存储路径: {}", fileNameWidthExtension);
    }

    /**
     * 解析复杂邮件内容
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public EmailAttachMentParams parseMesage() throws IOException, MessagingException {
        Object content = currentMessage.getContent();
        EmailAttachMentParams eamp = new EmailAttachMentParams();
        if (content instanceof MimeMultipart) {
            MimeMultipart multipart = (MimeMultipart) content;
            eamp = parseMultipart(multipart);
        }
        return eamp;
    }

    /**
     * 解析邮件内容
     * @param multipart
     * @return
     * @throws MessagingException
     * @throws IOException
     */
    public EmailAttachMentParams parseMultipart(Multipart multipart) throws MessagingException, IOException {
        EmailAttachMentParams eamp = new EmailAttachMentParams();
        int count = multipart.getCount();
        List<FileParam> list = new ArrayList<>();
        StringBuilder bodytext = new StringBuilder();
        for (int idx=0;idx<count;idx++) {
            BodyPart bodyPart = multipart.getBodyPart(idx);
            if (bodyPart.isMimeType(MIMETYPE_TEXT_PLAIN)) {
                bodytext.append((String) bodyPart.getContent());
            } else if(bodyPart.isMimeType(MIMETYPE_TEXT_HTML)) {
                bodytext.append((String) bodyPart.getContent());
            } else if(bodyPart.isMimeType(MIMETYPE_MULTIPART)) {
                Multipart mpart = (Multipart)bodyPart.getContent();
                EmailAttachMentParams emailAttachMentParams = parseMultipart(mpart);
                if (StringUtils.isNotEmpty(emailAttachMentParams)){
                    bodytext.append(emailAttachMentParams.getText());
                    list.addAll(Arrays.asList(emailAttachMentParams.getFiles()));
                }
            } else if (bodyPart.isMimeType(APPLICATION_OCTET_STREAM)) {
                String disposition = bodyPart.getDisposition();
                if (StringUtils.isNotEmpty(disposition)&&disposition.equalsIgnoreCase(BodyPart.ATTACHMENT)) {
                    String fileName = bodyPart.getFileName();
                    InputStreamReader sbis = new InputStreamReader(bodyPart.getInputStream());
                    list.add(saveFile(escp.getAttachmentDir()+fileName, sbis));
                }
            }
        }
        eamp.setText(bodytext.toString());
        eamp.setFiles(list.toArray(new FileParam [list.size()]));
        return eamp;
    }

    /**
     * 接收邮件
     * @descripted:只接收未读邮件
     *             开发过程中，此方法可添加线程池，给每封邮件起一个线程执行
     * @return
     */
    @Override
    public List<EmailAttachMentParams> receiverEmail(){
        List<EmailAttachMentParams> eps = new ArrayList<>();
        try {
            if (connect()){
                if (openInBoxFolder()){
                    messages = folder.getMessages();
                    log.info("邮件总数:{}",getMessageCount());
                    log.info("新邮件总数:{}",getNewMessageCount());
                    log.info("未读邮件总数:{}",getUnReadMessageCount());
                    //邮件下载出错计算器
                    int errorCounter = 0;
                    for (int index = 0;index<getMessageCount();index++){
                        try {
                            currentMessage = messages[index];//设置当前邮件
                            // 删除邮件
                            // currentMessage.setFlag(Flags.Flag.DELETED, true);
                            // 标记为已读[imap协议才可使用，pop3协议下无此功能]
                            // currentMessage.setFlag(Flags.Flag.SEEN, true);
                            if(!isNew()){
                                EmailAttachMentParams ep,ep2;
                                log.info("正在获取第{}封邮件",index);
                                //下载eml文件【此处并未打开下载eml文件，仅保存文件名】
                                saveMLFile(currentMessage);
                                //获取邮件内容
                                ep = formartEamilInfo();
                                ep2 = parseMesage();
                                if (StringUtils.isNotEmpty(ep2)){
                                    ep.setText(ep2.getText());
                                    ep.setFiles(ep2.getFiles());
                                }
                                eps.add(ep);
                                log.info("成功获取第{}封邮件",index);
                            }
                        } catch (Throwable e) {
                            errorCounter++;
                            log.info("下载第{}封邮件时出错",index);
                        }
                    }
                    log.info("失败下载了{}封邮件",errorCounter);
                }else{
                    log.info("打开收件箱异常!");
                }
            }else{
                log.info("邮件接收服务端登录失败!");
            }
        } catch (MessagingException e) {
            log.info("接收邮件抛出异常:{}",e.getMessage());
        } finally {
            close();
        }
        return eps;
    }

    /**
     * 测试
     * @param args
     * @throws IOException
     * @throws MessagingException
     */
    public static void main(String[] args) throws IOException, MessagingException {
        EmailServerConfigurationParams escp = new EmailServerConfigurationParams();
//        escp.setHost("pop.exmail.qq.com");
//        escp.setPort("993");
        escp.setHost("imap.exmail.qq.com");
        escp.setUserName("yinhaiquan@juzix.io");
        escp.setPassword("yhq123456");
//        escp.setProtocal("pop3");
        escp.setProtocal("imap");
        escp.setAttachmentDir("E:\\");
        escp.setEmailDir("E:\\");
        Properties javaMailProperties = new Properties();
//        javaMailProperties.put("mail.pop3.host",escp.getHost());
        javaMailProperties.put("mail.imap.host", escp.getHost());
        escp.setJavaMailProperties(javaMailProperties);
        TemplateMailReciverHandler mrsh = new TemplateMailReciverHandler(escp);
        List<EmailAttachMentParams> list = mrsh.receiverEmail();
        System.out.println(list.size());
    }
}
