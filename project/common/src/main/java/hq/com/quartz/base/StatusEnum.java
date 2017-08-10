package hq.com.quartz.base;

/**
 * @title : 任务开关状态扭矩枚举
 * @describle :
 * <p>
 *     <b>note:</b>
 *     启动 initialing ----> running
 *     关闭 running    ----> shutdowning
 *     暂停 running    ----> pausing
 *     重启 pausing    ----> running
 * </p>
 * Create By yinhaiquan
 * @date 2017/8/10 14:48 星期四
 */
public enum StatusEnum {
    INITIALING("initialing","初始化中"),
    RUNNING("running","运行中"),
    PAUSING("pausing","暂停中"),
    SHUTDOWNING("shutdowning","关闭中");

    private String code;
    private String desc;
    private StatusEnum(String code,String desc){
        this.code = code;
        this.desc = desc;
    }

    public static StatusEnum newInstance(String code){
        StatusEnum [] ses = values();
        for (StatusEnum statusEnum : ses) {
            if (statusEnum.getCode().equals(code)){
                return statusEnum;
            }
        }
        return null;
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
