package hq.com.quartz.listeners;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/8 18:33 星期二
 */
public class QuartzJobListener implements JobListener{
    private String name;

    public QuartzJobListener(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * <1
     * 任务执行之前执行
     * @param jobExecutionContext
     */
    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        System.out.println("jobToBeExecuted");
    }

    /**
     * <2
     * 这个方法正常情况下不执行,但是如果当TriggerListener中的vetoJobExecution方法返回true时,那么执行这个方法
     * 需要注意的是 如果方法(2)执行 那么(1),(3)这个俩个方法不会执行,因为任务被终止了
     * @param jobExecutionContext
     */
    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {
        System.out.println("jobExecutionVetoed");
    }

    /**
     * <3
     * 任务执行完成后执行,jobException如果它不为空则说明任务在执行过程中出现了异常
     * @param jobExecutionContext
     * @param e
     */
    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        System.out.println("jobWasExecuted");
    }
}
