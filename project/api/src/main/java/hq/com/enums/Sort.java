package hq.com.enums;

import hq.com.aop.utils.StringUtils;

/**
 * @title : 排序类型枚举
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/9 11:59 星期六
 */
public enum Sort {
    ASC("asc","升序"),
    DESC("desc","降序");
    private String code;
    private String desc;

    private Sort(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public final static Sort newInstance(String code) {
        Sort[] s_ = Sort.values();
        if (StringUtils.isNotEmpty(s_)) {
            for (Sort s_s : s_) {
                if (s_s.getCode().equalsIgnoreCase(code)) {
                    return s_s;
                }
            }
        }
        return DESC;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
