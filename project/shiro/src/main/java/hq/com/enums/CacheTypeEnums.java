package hq.com.enums;

/**
 * @Describle: 缓存类型
 * @Author: YinHq
 * @Date: Created By 下午 2:08 2017/6/4 0004
 * @Modified By
 */
public enum CacheTypeEnums {
    SESSIONCACHE(1, "session缓存"),
    AUTHORIZATIONCACHE(0, "权限缓存");

    private int code;
    private String desc;

    private CacheTypeEnums(int code, String desc) {
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
