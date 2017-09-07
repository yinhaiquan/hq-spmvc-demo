package hq.com.auth.po;

import java.io.Serializable;
import java.util.Date;

/**
 * @title : 接口权限认证
 * @describle : t_interface_auth
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/7 13:51 星期四
 */
public class InterfaceAuth implements Serializable{
    private static final long serialVersionUID = -7741764465918078028L;
    /*n_id*/
    private int id;
    /*接口名称c_interface_name*/
    private String interfaceName;
    /*是否签名 0是 1否 b_iSign*/
    private boolean iSign;
    /*是否验证登录token 0是 1否b_isLogin*/
    private boolean isLogin;
    /*更新日期t_update_date*/
    private Date updateDate;
    /*创建日期t_create_date*/
    private Date createDate;

    public InterfaceAuth() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public boolean isiSign() {
        return iSign;
    }

    public void setiSign(boolean iSign) {
        this.iSign = iSign;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "InterfaceAuth{" +
                "id=" + id +
                ", interfaceName='" + interfaceName + '\'' +
                ", iSign=" + iSign +
                ", isLogin=" + isLogin +
                ", updateDate=" + updateDate +
                ", createDate=" + createDate +
                '}';
    }
}
