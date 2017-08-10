package hq.com.quartz.vo;

import hq.com.quartz.base.StatusEnum;
import org.quartz.Scheduler;

import java.io.Serializable;

/**
 * @title : 任务属性实体类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/10 15:25 星期四
 */
public class SchedulerInfo implements Serializable {
    private static final long serialVersionUID = -4368856753342566541L;
    /*任务ID*/
    private String schedulerId;
    /*任务名称 唯一取值*/
    private String jobName;
    /*任务类全包名+类名*/
    private Class<?> jobClass;
    /*任务组名*/
    private String jobGroupName;
    /*触发器组名*/
    private String triggerGroupName;
    /*触发时间*/
    private String time;
    /*任务实时状态*/
    private StatusEnum statusEnum;
    /*任务实体类*/
    private Scheduler scheduler;

    public SchedulerInfo() {
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

    public Class<?> getJobClass() {
        return jobClass;
    }

    public void setJobClass(Class<?> jobClass) {
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

    public StatusEnum getStatusEnum() {
        return statusEnum;
    }

    public void setStatusEnum(StatusEnum statusEnum) {
        this.statusEnum = statusEnum;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public String toString() {
        return "SchedulerInfo{" +
                "schedulerId='" + schedulerId + '\'' +
                ", jobName='" + jobName + '\'' +
                ", jobClass=" + jobClass +
                ", jobGroupName='" + jobGroupName + '\'' +
                ", triggerGroupName='" + triggerGroupName + '\'' +
                ", time='" + time + '\'' +
                ", statusEnum=" + statusEnum +
                ", scheduler=" + scheduler +
                '}';
    }
}
