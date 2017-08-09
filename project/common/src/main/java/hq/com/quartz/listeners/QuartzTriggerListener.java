package hq.com.quartz.listeners;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.TriggerListener;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/8 18:34 星期二
 */
public class QuartzTriggerListener implements TriggerListener {
    private String name;

    public QuartzTriggerListener(String name) {
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
     * 当与监听器相关联的 Trigger 被触发，Job 上的 execute() 方法将要被执行时，
     * Scheduler 就调用这个方法。在全局 TriggerListener 情况下，这个方法为所有
     * Trigger 被调用
     *
     * @param trigger
     * @param jobExecutionContext
     */
    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext jobExecutionContext) {
        System.out.println("triggerFired");
    }

    /**
     * <2
     * 在 Trigger 触发后，Job 将要被执行时由 Scheduler 调用这个方法。TriggerListener
     * 给了一个选择去否决 Job 的执行。假如这个方法返回 true，这个 Job 将不会为此次 Trigger
     * 触发而得到执行
     *
     * @param trigger
     * @param jobExecutionContext
     * @return
     */
    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext jobExecutionContext) {
        return false;
    }

    /**
     * <3
     * Scheduler 调用这个方法是在 Trigger 错过触发时。如这个方法的 JavaDoc 所指出的，
     * 你应该关注此方法中持续时间长的逻辑：在出现许多错过触发的 Trigger 时，长逻辑会导致
     * 骨牌效应。你应当保持这上方法尽量的小
     *
     * @param trigger
     */
    @Override
    public void triggerMisfired(Trigger trigger) {
        System.out.println("triggerMisfired");
    }

    /**
     * <4
     * Trigger 被触发并且完成了 Job 的执行时，Scheduler 调用这个方法。这不是说这个 Trigger
     * 将不再触发了，而仅仅是当前 Trigger 的触发(并且紧接着的 Job 执行) 结束时。这个 Trigger
     * 也许还要在将来触发多次的
     *
     * @param trigger
     * @param jobExecutionContext
     * @param i
     */
    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext jobExecutionContext, int i) {
        System.out.println("triggerComplete");
    }
}
