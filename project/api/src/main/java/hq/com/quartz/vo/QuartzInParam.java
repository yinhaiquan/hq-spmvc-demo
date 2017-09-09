package hq.com.quartz.vo;

import hq.com.aop.annotation.Bean;
import hq.com.aop.annotation.Parameter;
import hq.com.aop.aopenum.ParamType;
import hq.com.aop.vo.InParam;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @title : 定时任务入参映射实体
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/10 17:18 星期四
 */
@Bean
@Component("quartzInfo")
public class QuartzInParam extends InParam implements Serializable {
    private static final long serialVersionUID = 1891080027779161597L;
    @Parameter(value = "schedulerId", type = ParamType.STRING, desc = "任务ID")
    private String schedulerId;
    @Parameter(value = "jobName", type = ParamType.STRING, desc = "任务名称")
    private String jobName;
    @Parameter(value = "jobClass", type = ParamType.STRING, desc = "任务全包名+类名")
    private String jobClass;
    @Parameter(value = "jobGroupName", type = ParamType.STRING, desc = "任务组名")
    private String jobGroupName;
    @Parameter(value = "triggerGroupName", type = ParamType.STRING, desc = "触发组名")
    private String triggerGroupName;
    @Parameter(value = "time", type = ParamType.STRING, desc = "触发时间")
    private String time;
    @Parameter(value = "switchType", type = ParamType.INT, desc = "开关类型")
    private int switchType;

    public QuartzInParam() {
    }

    public String getSchedulerId() {
        return schedulerId;
    }

    public void setSchedulerId(String schedulerId) {
        this.schedulerId = schedulerId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobClass() {
        return jobClass;
    }

    public void setJobClass(String jobClass) {
        this.jobClass = jobClass;
    }

    public String getJobGroupName() {
        return jobGroupName;
    }

    public void setJobGroupName(String jobGroupName) {
        this.jobGroupName = jobGroupName;
    }

    public String getTriggerGroupName() {
        return triggerGroupName;
    }

    public void setTriggerGroupName(String triggerGroupName) {
        this.triggerGroupName = triggerGroupName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSwitchType() {
        return switchType;
    }

    public void setSwitchType(int switchType) {
        this.switchType = switchType;
    }

    @Override
    public String toString() {
        return "QuartzInParam{" +
                "schedulerId='" + schedulerId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobClass='" + jobClass + '\'' +
                ", jobGroupName='" + jobGroupName + '\'' +
                ", triggerGroupName='" + triggerGroupName + '\'' +
                ", time='" + time + '\'' +
                ", switchType=" + switchType +
                '}'+super.toString();
    }
}
