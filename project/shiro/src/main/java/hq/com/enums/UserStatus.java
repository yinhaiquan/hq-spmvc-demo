package hq.com.enums;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 2:08 2017/6/4 0004
 * @Modified By
 */
public enum UserStatus {
    EFFECTIVE(1, "有效"),
    INVALID(0, "无效");

    private int code;
    private String desc;

    private UserStatus(int code, String desc) {
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
}
