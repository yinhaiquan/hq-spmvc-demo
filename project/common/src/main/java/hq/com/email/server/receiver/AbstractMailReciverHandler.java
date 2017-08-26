package hq.com.email.server.receiver;

import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.FileParam;
import hq.com.email.vo.EmailParams;
import hq.com.email.vo.EmailServerConfigurationParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeUtility;
import java.io.*;

/**
 * @title : 邮件接收服务处理器
 * @describle :
 * <p>
 *     <b>note:</b>
 *     1. 支持pop3、imap协议
 *     2. 支持接收纯文本、html以及带有附件邮件
 * </p>
 * Create By yinhaiquan
 * @date 2017/8/25 11:42 星期五
 */
public abstract class AbstractMailReciverHandler {
    protected static Logger log = LoggerFactory.getLogger(AbstractMailReciverHandler.class);
    protected static final String FOLDER = "INBOX";
    protected static final String POINT = ".";
    protected static final String SLASH = "/";
    protected static final String LEFT = "<";
    protected static final String RIGHT = ">";
    protected static final String MIMETYPE_MULTIPART = "multipart/*";
    protected static final String MIMETYPE_MESSAGE = "message/rfc822";
    protected static final String HEADER = "Disposition-Notification-To";
    protected static final String MIMETYPE_TEXT_PLAIN = "text/plain";
    protected static final String MIMETYPE_TEXT_HTML = "text/html";
    protected static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    private static final boolean isDebug = false;
    private static final int FOLDER_TYPE = Folder.READ_ONLY;

    /*邮件服务器配置信息*/
    protected EmailServerConfigurationParams escp;
    /*连接邮件服务器后的邮箱*/
    protected Store store;
    /*收件箱*/
    protected Folder folder;
    /*收件箱中的邮件消息*/
    protected Message[] messages;
    /*当前正在处理的邮件消息*/
    protected Message currentMessage;
    /*当前邮件文件名*/
    protected String currentEmailFileName;

    public AbstractMailReciverHandler(EmailServerConfigurationParams escp) {
        this.escp = escp;
    }

    /**
     * 登录邮箱服务器
     * @return
     */
    public final boolean connect(){
        try {
            Session session = Session.getInstance(escp.getJavaMailProperties(),null);
            session.setDebug(isDebug);
            store = session.getStore(escp.getProtocal());
            if (escp.isValidate()){
                store.connect(escp.getUserName(),escp.getPassword());
            }
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    /**
     * 打开收件箱
     * @return
     */
    public final boolean openInBoxFolder(){
        try {
            folder = store.getFolder(FOLDER);
            folder.open(FOLDER_TYPE);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    /**
     * 关闭邮箱
     * @return
     */
    public final boolean close(){
        try {
            if (StringUtils.isNotEmpty(folder)&&folder.isOpen()){
                folder.close(true);
                store.close();
            }
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }

    /**
     * 保存文件
     * @param fileName 文件名
     * @param input 数据流
     */
    public final FileParam saveFile(String fileName , Reader input){
        FileParam fp = new FileParam();
        // 为了放置文件名重名，在重名的文件名后面天上数字
        File file = new File(fileName);
        // 先取得文件名的后缀
        int lastDot = fileName.lastIndexOf(POINT);
        String extension = fileName.substring(lastDot);
        fileName = fileName.substring(0, lastDot);
        for (int i = 0; file.exists(); i++) {
            //　如果文件重名，则添加i
            file = new File(fileName + i + extension);
        }
        BufferedWriter bos = null;
        BufferedReader bis = null;
        try {
            // 从输入流中读取数据，写入文件输出流
            FileWriter fos = new FileWriter(file);
            bos = new BufferedWriter(fos);
            bis = new BufferedReader(input);
            int aByte;
            while ((aByte = bis.read()) != -1) {
                bos.write(aByte);
            }
        } catch (IOException e) {
            log.info("保存文件异常：{}",e.getMessage());
        } finally {
            // 关闭流
            try {
                bos.flush();
                bos.close();
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fp.setFileName(file.getName());
        fp.setFileLocation(file.getPath());
        return fp;
    }

    /**
     * 获取文件名
     * @param part
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public final String getFileName(Part part) throws MessagingException, UnsupportedEncodingException {
        String fileName = part.getFileName();
        String name = fileName;
        if (StringUtils.isNotEmpty(fileName)) {
            fileName = MimeUtility.decodeText(fileName);
            int index = fileName.lastIndexOf(SLASH);
            if (index != -1) {
                name = fileName.substring(index + 1);
            }
        }
        return name;
    }

    /**
     * 获得尖括号之间的字符
     * @param str
     */
    public final String getInfoBetweenBrackets(String str){
        int i ,j; //用于标识字符串中的"<"和">"的位置
        if (StringUtils.isEmpty(str)){
            return "undefined";
        }
        i = str.lastIndexOf(LEFT);
        j = str.lastIndexOf(RIGHT);
        if (i!=-1&&j!=-1){
            str = str.substring(i+1,j);
        }
        return str;
    }


    /**
     * 判断是否包含附件 true 包含附件 false 不包含附件
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public final boolean isContainAttach() throws IOException, MessagingException {
        return isContainAttach(currentMessage);
    }

    public final boolean isContainAttach(Part part) throws MessagingException, IOException {
        boolean attachflag = false;
        if (part.isMimeType(MIMETYPE_MULTIPART)){
            Multipart mp = (Multipart) part.getContent();
            for (int i =0 ;i<mp.getCount();i++){
                BodyPart bodyPart = mp.getBodyPart(i);
                String disposition = bodyPart.getDisposition();
                if (StringUtils.isNotEmpty(disposition)&& ((disposition.equals(Part.ATTACHMENT)) || (disposition
                        .equals(Part.INLINE)))){
                    attachflag = true;
                }else if (bodyPart.isMimeType(MIMETYPE_MULTIPART)){
                    attachflag = isContainAttach(bodyPart);
                }else{
                    String contype = bodyPart.getContentType();
                    if (contype.toLowerCase().indexOf("application")!=-1){
                        attachflag = true;
                    }
                    if (contype.toLowerCase().indexOf("name")!=-1){
                        attachflag = true;
                    }
                }
            }
        }else if(part.isMimeType(MIMETYPE_MESSAGE)){
            attachflag = isContainAttach((Part) part.getContent());
        }
        return attachflag;
    }


    /**
     * 判断邮件是否需要回执 true 需要 false 不需要
     * @return
     * @throws MessagingException
     */
    public final boolean getReplySign() throws MessagingException {
        String needreply [] = currentMessage.getHeader(HEADER);
        if (StringUtils.isNotEmpty(needreply)){
            return true;
        }
        return false;
    }

    /**
     * 判断邮件是否已读 true已读 false未读
     * pop3没有判断邮件是否为已读的功能，要使用Imap才可以
     * @return
     * @throws MessagingException
     */
    public final boolean isNew() throws MessagingException {
        Flags flags = currentMessage.getFlags();
        Flags.Flag [] flag = flags.getSystemFlags();
        for (Flags.Flag f : flag) {
            if (f == Flags.Flag.SEEN){
                return true;
            }
        }
        return false;
    }

    /**
     * 获取地址
     * @param type
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public final String [] getAddress(Message.RecipientType type) throws MessagingException, UnsupportedEncodingException {
        InternetAddress[] addresses = (InternetAddress[]) currentMessage.getRecipients(type);
        if (StringUtils.isEmpty(addresses)){
            return null;
        }
        String [] ads = new String[addresses.length];
        for (int i=0;i<addresses.length;i++) {
            //邮件地址
            String email = addresses[i].getAddress();
            email = MimeUtility.decodeText(email);
            //个人描述信息
            String personal = addresses[i].getPersonal();
            StringBuffer sb = new StringBuffer();
            if (StringUtils.isNotEmpty(personal)){
                personal = MimeUtility.decodeText(personal);
                sb.append(personal);
            }
            sb.append(LEFT).append(email).append(RIGHT);
            ads[i]=sb.toString();
        }
        return ads;
    }

    /**
     * 获取发件人信息
     * @return
     * @throws MessagingException
     */
    public final String getFrom() throws MessagingException {
        InternetAddress[] addresses = (InternetAddress[]) currentMessage.getFrom();
        StringBuffer sb = new StringBuffer();
        //获取发件人邮箱
        String from = addresses[0].getAddress();
        //发件人描述信息
        String personal = addresses[0].getPersonal();
        if (StringUtils.isNotEmpty(personal)){
            sb.append(personal);
        }
        sb.append(LEFT).append(from).append(RIGHT);
        return sb.toString();
    }

    /**
     * 获取收件箱中邮件数量
     * @return
     */
    public final int getMessageCount(){
        return messages.length;
    }

    /**
     * 获取收件箱中新邮件的数量
     * @return
     * @throws MessagingException
     */
    public final int getNewMessageCount() throws MessagingException {
        return folder.getNewMessageCount();
    }

    /**
     * 获取收件箱中未读邮件数量
     * @return
     * @throws MessagingException
     */
    public final int getUnReadMessageCount() throws MessagingException {
        return folder.getUnreadMessageCount();
    }

    /**
     * 获取邮件主题
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    public final String getSubject() throws MessagingException, UnsupportedEncodingException {
        return MimeUtility.decodeText(currentMessage.getSubject());
    }

    /**
     * 获取邮件详情
     * @return
     */
    public abstract EmailParams formartEamilInfo() throws MessagingException, UnsupportedEncodingException;

    /**
     * 保存邮件源文件eml
     */
    public abstract void saveMLFile(Message message) throws MessagingException, IOException;

    /**
     * 接收邮件
     * @return
     */
    public abstract EmailParams receiverEmail() throws MessagingException;
}
