package hq.com.quartz.base;

/**
 * @title : 任务开关类型枚举
 * @describle :
 * Create By yinhaiquan
 * @date 2017/8/10 14:48 星期四
 */
public enum SwitchTypeEnum {
    START(1,"启动"),
    STARTALL(11,"启动所有"),
    STOP(2,"暂停"),
    STOPALL(22,"暂停所有"),
    SHUTDOWN(3,"关闭"),
    SHUTDOWNALL(33,"关闭所有"),
    RESUME(4,"重启"),
    RESUMEALL(44,"重启所有");

    private int code;
    private String desc;
    private SwitchTypeEnum(int code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public static SwitchTypeEnum newInstance(int code){
        SwitchTypeEnum[] ses = values();
        for (SwitchTypeEnum statusEnum : ses) {
            if (statusEnum.getCode()==(code)){
                return statusEnum;
            }
        }
        return null;
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
