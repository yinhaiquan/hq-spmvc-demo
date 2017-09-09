package hq.com.auth.dto;

import hq.com.aop.annotation.TableField;

import java.io.Serializable;

/**
 * @title : 接口权限安全管理对外实体
 * @describle :
 * <p>
 *     <b>note:</b>
 *     对外暴露实体中需要排序的字段须使用@TableField注解对应表字段
 * </p>
 * Create By yinhaiquan
 * @date 2017/9/9 15:34 星期六
 */
public class InterfaceAuthDto implements Serializable {
    private static final long serialVersionUID = 6300962937383696939L;
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
    private String updateDate;
    /*创建日期*/
    @TableField("t_create_date")
    private String createDate;

    public InterfaceAuthDto() {
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "InterfaceAuthDto{" +
                "id=" + id +
                ", methodName='" + methodName + '\'' +
                ", className='" + className + '\'' +
                ", iSign=" + iSign +
                ", isLogin=" + isLogin +
                ", updateDate='" + updateDate + '\'' +
                ", createDate='" + createDate + '\'' +
                '}';
    }
}
