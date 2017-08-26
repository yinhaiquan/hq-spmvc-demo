package hq.com.email.vo;

import java.io.Serializable;

/**
 * @Describle: 邮件内容实体类
 * @Author: yinhaiquan
 * @Date: Created By 下午 12:59 2017/8/26 0026
 * @Modified By
 */
public class EmailAttachMentParams extends EmailParams implements Serializable {
    private static final long serialVersionUID = 5915826984289824827L;
    /*邮箱服务器邮件ID*/
    private String emailServerId;

    public String getEmailServerId() {
        return emailServerId;
    }

    public void setEmailServerId(String emailServerId) {
        this.emailServerId = emailServerId;
    }

    @Override
    public String toString() {
        return "EmailAttachMentParams{" +
                "emailServerId='" + emailServerId + '\'' +
                '}'+'\n'+super.toString();
    }
}
