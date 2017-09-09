package hq.com.enums;

import hq.com.aop.utils.StringUtils;

/**
 * @title : 美猴王状态枚举
 * @describle :
 * <p>
 *     <b>note:</b>
 *             真假美猴王
 * </p>
 * Create By yinhaiquan
 * @date 2017/9/8 14:42 星期五
 */
public enum MonkeyStatus {
    TRUE(1,"真猴子"),
    FALSE(0,"假猴子");


    private int code;
    private String desc;

    private MonkeyStatus(int code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public final static MonkeyStatus newInstance(int code) {
        MonkeyStatus[] s_ = MonkeyStatus.values();
        if (StringUtils.isNotEmpty(s_)) {
            for (MonkeyStatus s_s : s_) {
                if (s_s.getCode() == code) {
                    return s_s;
                }
            }
        }
        return FALSE;
    }

    public boolean getBool(){
        return 1==code?true:false;
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
