package hq.com.email.server.receiver;

import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.FileParam;
import hq.com.email.vo.EmailParams;
import hq.com.email.vo.EmailServerConfigurationParams;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
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
    public void parseMessage(Message message) throws IOException, MessagingException {
        Object content = message.getContent();
        if (content instanceof Multipart){
            handlerMultipart((Multipart) content);
        }else{
            parsePart(message);
        }
    }

    /**
     * 解析指定part,从中提取文件
     * @param part
     */
    @Override
    public FileParam parsePart(Part part) throws IOException, MessagingException {
        String disposition = part.getDisposition();
        String contentType = part.getContentType();
        FileParam fp = new FileParam();
        String fileNameWidthExtension = "";
        // 获得邮件的内容输入流
        InputStreamReader sbis = new InputStreamReader(part.getInputStream());
        // 没有附件的情况 保存邮件内容
        if (StringUtils.isEmpty(disposition)){
            if (contentType.length()>=10&& (contentType.toLowerCase().substring(0, 10)
                    .equals(MIMETYPE_TEXT_PLAIN))){
                fileNameWidthExtension = escp.getAttachmentDir()+currentEmailFileName+TXT_SUFFIX;
            }else if (contentType.length()>=9&& (contentType.toLowerCase().substring(0, 9)
                    .equals(MIMETYPE_TEXT_HTML))){
                // Check if html
                fileNameWidthExtension = escp.getAttachmentDir()+this.currentEmailFileName+HTML_SUFFIX;
            }else if (contentType.length()>=9&& (contentType.toLowerCase().substring(0, 9)
                    .equals(MIMETYPE_IMAGE_GIF))){
                fileNameWidthExtension = escp.getAttachmentDir()+currentEmailFileName+GIF_SUFFIX;
            }else if (contentType.length()>=11&& contentType.toLowerCase().substring(0, 11).equals(
                    MIMETYPE_MULTIPART)){
                handlerMultipart((Multipart) part.getContent());
            }else{
                // Unknown type
                fileNameWidthExtension = escp.getAttachmentDir()+currentEmailFileName+TXT_SUFFIX;
            }
            fp = saveFile(fileNameWidthExtension,sbis);
            return fp;
        }
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
//                            saveMLFile(currentMessage);
                            EmailParams ep = formartEamilInfo();
                            //下载邮件文件附件等
                            parseMessage(currentMessage);
                            File html = new File(escp.getAttachmentDir()+this.currentEmailFileName+HTML_SUFFIX);
                            File text = new File(escp.getAttachmentDir()+this.currentEmailFileName+TXT_SUFFIX);
                            if (html.exists()){
                                InputStream is = new FileInputStream(html);
                                ep.setHtml(true);
                                ep.setText(inputStreamToHtml(is));
                            }
                        if (text.exists()){
                            InputStream is = new FileInputStream(text);
                            ep.setHtml(false);
                            ep.setText(inputStreamToHtml(is));
                        }

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

    private String inputStreamToHtml(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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
