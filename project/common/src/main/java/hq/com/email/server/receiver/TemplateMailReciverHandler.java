package hq.com.email.server.receiver;

import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.FileParam;
import hq.com.email.vo.EmailParams;
import hq.com.email.vo.EmailServerConfigurationParams;

import javax.mail.*;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.Properties;

/**
 * @title : 邮件接收服务处理器
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/25 14:24 星期五
 */
public class TemplateMailReciverHandler extends AbstractMailReciverHandler{
    public TemplateMailReciverHandler(EmailServerConfigurationParams escp) {
        super(escp);
    }

    /**
     * 获取邮件详情
     *
     * @return
     */
    @Override
    public EmailParams formartEamilInfo() throws MessagingException, UnsupportedEncodingException {
        EmailParams ep = new EmailParams();
        ep.setHtml(true);
        ep.setEmailServerId(((MimeMessage)currentMessage).getMessageID());
        ep.setSentDate(currentMessage.getSentDate());
        ep.setSubject(getSubject());
        ep.setFrom(getFrom());
        ep.setReceiver(getAddress(Message.RecipientType.TO));
        ep.setCc(getAddress(Message.RecipientType.CC));
        ep.setBcc(getAddress(Message.RecipientType.BCC));
        ep.setFiles(null);
        return ep;
    }

    /**
     * 保存邮件源文件eml
     */
    @Override
    public void saveMLFile(Message message) throws MessagingException, IOException {
        // 将邮件的ID中尖括号中的部分做为邮件的文件名
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
        System.out.println("邮件消息的存储路径: " + fileNameWidthExtension);
        // 将邮件消息的内容写入ByteArrayOutputStream流中
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        message.writeTo(baos);
        // 读取邮件消息流中的数据
        StringReader in = new StringReader(baos.toString());
        // 存储到文件
        saveFile(fileNameWidthExtension, in);
    }

    /**
     * 解析message
     */
    @Override
    public String parseMessage(Part message) throws IOException, MessagingException {
        String disposition = message.getDisposition();
        String contentType = message.getContentType();
        int nameindex = contentType.indexOf("name");
        boolean conname = false;
        if (nameindex!=-1){
            conname = true;
        }
        StringBuilder bodytext = new StringBuilder();
        // 没有附件的情况 保存邮件内容
        if (StringUtils.isEmpty(disposition)){
            if (message.isMimeType(MIMETYPE_TEXT_PLAIN)&&!conname){
                bodytext.append((String) message.getContent());
            }else if (message.isMimeType(MIMETYPE_TEXT_HTML)&&!conname){
                bodytext.append((String) message.getContent());
            }else if (message.isMimeType(MIMETYPE_MULTIPART)){
                Multipart multipart = (Multipart) message.getContent();
                for (int i =0;i<multipart.getCount();i++){
                    bodytext.append(parseMessage( multipart.getBodyPart(i)));
                }
            }else if (message.isMimeType(MIMETYPE_MESSAGE)){
                bodytext.append(parseMessage(message));
            }else{
                log.info("未知文件类型");
            }
        }
        return bodytext.toString();
    }

    /**
     * 解析指定part,从中提取附件
     * @param part
     */
    @Override
    public FileParam parsePart(Part part) throws IOException, MessagingException {
        String disposition = part.getDisposition();
        FileParam fp = new FileParam();
        String fileNameWidthExtension = null;
        // 获得邮件的内容输入流
        InputStreamReader sbis = new InputStreamReader(part.getInputStream());
        // 各种有附件的情况
        String name = getFileName(part);
        if (disposition.equalsIgnoreCase(Part.ATTACHMENT)||disposition.equalsIgnoreCase(Part.INLINE)){
            fileNameWidthExtension = escp.getAttachmentDir()+name;
        }
        // 存储各类附件
        if (StringUtils.isNotEmpty(fileNameWidthExtension)){
            fp = saveFile(fileNameWidthExtension,sbis);
        }
        return fp;
    }

    /**
     * 接收邮件
     *
     * @return
     */
    @Override
    public EmailParams receiverEmail() throws MessagingException {
        if (connect()){
            if (openInBoxFolder()){
                messages = folder.getMessages();
                System.out.println("邮件总数:"+getMessageCount());
                System.out.println("新邮件总数:"+getNewMessageCount());
                System.out.println("未读邮件总数:"+getUnReadMessageCount());
                //邮件下载出错计算器
                int errorCounter = 0;
                //邮件下载成功计算器
                int successCounter = 0;
                for (int index = 0;index<getMessageCount();index++){
                    try {
                        currentMessage = messages[index];//设置当前邮件
                        System.out.println("正在获取第" + index + "封邮件");
//                        if(!isNew()){
                            //下载eml文件
                            saveMLFile(currentMessage);
                            EmailParams ep = formartEamilInfo();
                            //下载邮件文件附件等
                            //判断是否包含附件
                            if (isContainAttach(currentMessage)){
                                parsePart(currentMessage);
                            }
                            //获取邮件内容
                            String sb = parseMessage(currentMessage);
                            ep.setText(sb);
                            System.out.println("邮件内容:");
                            System.out.println(ep.getText());
                            System.out.println(ep);
//                        }
                        System.out.println("成功获取第" + index + "封邮件");
                        successCounter++;
                    } catch (Throwable e) {
                        e.printStackTrace();
                        errorCounter++;
                        System.err.println("下载第" + index + "封邮件时出错");
                    }
                }
                System.out.println("------------------");
                System.out.println("成功下载了" + successCounter + "封邮件");
                System.out.println("失败下载了" + errorCounter + "封邮件");
                System.out.println("------------------");
                close();
            }
        }
        return null;
    }

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
        mrsh.receiverEmail();
    }
}
