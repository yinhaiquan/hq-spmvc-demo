package hq.com.email.server;

/**
 * @title : 公共邮件抽象类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/28 11:01 星期一
 */
public abstract class CommenMailHandler {
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
}
