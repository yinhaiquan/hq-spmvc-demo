package hq.com.enums;

import hq.com.aop.utils.StringUtils;

/**
 * @title : 系统状态枚举
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/6/30 13:59 星期五
 */
public enum SystemCodeEnum {
    SYSTEM_ERROR(-1, "request error"),
    SYSTEM_OK(0, "response ok"),
    SYSTEM_USER_NMAE_PASSWORD_ERROR(1,"用户名/密码错误"),
    SYSTEM_USER_UNKNOWN_ACCOUNT(2,"账号不存在"),
    SYSTEM_USER_DISABLED_ACCOUNT(3,"账号被禁用"),
    SYSTEM_USER_EXCESSIVE_ATTEMPTS(4,"超过登录失败次数限制"),
    SYSTEM_PARAMS_NOT_EXIST(5,"缺失参数");

    private int code;
    private String desc;

    private SystemCodeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public final static SystemCodeEnum newInstance(int code) {
        SystemCodeEnum[] s_ = SystemCodeEnum.values();
        if (StringUtils.isNotEmpty(s_)) {
            for (SystemCodeEnum s_s : s_) {
                if (s_s.getCode() == code) {
                    return s_s;
                }
            }
        }
        return null;
    }
}
