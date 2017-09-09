package hq.com.auth.po;

import hq.com.aop.annotation.TableField;
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
    @TableField("n_id")
    private int id;
    /*接口名称*/
    @TableField("c_method_name")
    private String methodName;
    /*接口类名*/
    @TableField("c_class_name")
    private String className;
    /*是否签名 0是 1否*/
    @TableField("b_iSign")
    private boolean iSign;
    /*是否验证登录token 0是 1否*/
    @TableField("b_isLogin")
    private boolean isLogin;
    /*更新日期*/
    @TableField("t_update_date")
    private Date updateDate;
    /*创建日期*/
    @TableField("t_create_date")
    private Date createDate;

    public InterfaceAuth() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "InterfaceAuth{" +
                "id=" + id +
                ", methodName='" + methodName + '\'' +
                ", className='" + className + '\'' +
                ", iSign=" + iSign +
                ", isLogin=" + isLogin +
                ", updateDate=" + updateDate +
                ", createDate=" + createDate +
                '}';
    }
}
