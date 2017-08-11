package hq.com.quartz;

import hq.com.aop.utils.StringUtils;
import hq.com.quartz.listeners.QuartzJobListener;
import hq.com.quartz.listeners.QuartzTriggerListener;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @title : 定时任务集成管理类
 * @describle :
 * <p>
 *     <b>note:</b>
 *     机房灾难、服务宕机等导致服务需要重启情形：
 *              <1 : 定时任务未存储于表中集成管理时，可重新添加定时任务并启动
 *              <2 : 定时任务存储于表中集成管理时，可在服务重启时根据表中定时任务列表初始化至定时任务存储容器
 *
 *     注意【任务状态】：
 *     a. 启动前状态：
 *           isStarted: false
 *           isShutdown: false
 *           isInStandbyMode: true
 *
 *     b. 启动后状态：
 *           isStarted: true
 *           isShutdown: false
 *           isInStandbyMode: false
 *
 *     c. 关闭后状态：
 *           isStarted: true
 *           isShutdown: true
 *           isInStandbyMode: true
 *     d. 暂停后状态：
 *           isStarted: true
 *           isShutdown: false
 *           isInStandbyMode: false
 * </p>
 * Create By yinhaiquan
 * @date 2017/8/8 17:03 星期二
 */
public final class QuartzManager {
    private static Logger log = LoggerFactory.getLogger(QuartzManager.class);
    /**定时任务总调度器*/
    private static StdSchedulerFactory factory = new StdSchedulerFactory();
    public static String DEFAULT_JOB_GROUP_NAME = "DEFAULT";
    public static String DEFAULT_TRIGGER_GROUP_NAME = "DEFAULT";
    public static String JOB_LISTENER_SUFFIX = "_JobListener";
    public static String TRIGGER_LISTENER_SUFFIX = "_TriggerListener";

    /** 所有集成定时任务存储容器,重启服务可将定时任务列表初始化至此*/
    public static ConcurrentMap<String,Scheduler> map = new ConcurrentHashMap<>();

    /**
     * 添加定时任务
     * @param schedulerId       任务ID 必须唯一 通过此ID开启任务等操作
     * @param jobName           任务名称
     * @param cls               任务类全包名+类名
     * @param time              触发时间
     * @param jobGroupName      任务组名
     * @param triggerGroupName  触发器组名
     */
    public final static void addJob(String schedulerId,String jobName,
                              String jobGroupName,String triggerGroupName,
                              Class cls,String time){
        try{
            /**
             * 实现自定义scheduler名称属性配置，默认DefaultQuartzScheduler同一个实例，
             * 因此按照默认配置所有定时任务属于同一个实例，使用start(),shutdown()是启动、
             * 关闭所有定时任务。
             *
             * 实现启动、关闭指定定时任务则须自定义属性配置文件，如下:
             */
            Properties props = new Properties();
            props.put("org.quartz.scheduler.instanceName", schedulerId);
            props.put("org.quartz.scheduler.rmi.export", false);
            props.put("org.quartz.scheduler.rmi.proxy", false);
            props.put("org.quartz.scheduler.wrapJobExecutionInUserTransaction", false);
            props.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
            props.put("org.quartz.threadPool.threadCount", "10");
            props.put("org.quartz.threadPool.threadPriority", "5");
            props.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", true);
            props.put("org.quartz.jobStore.misfireThreshold", "60000");
            props.put("org.quartz.jobStore.class", "org.quartz.simpl.RAMJobStore");
            factory.initialize(props);
            Scheduler scheduler = factory.getScheduler();
            JobDetail jobDetail = new JobDetail(jobName,StringUtils.isNotEmpty(jobGroupName)?jobGroupName:DEFAULT_JOB_GROUP_NAME,cls);
            CronTrigger cronTrigger = new CronTrigger(jobName,StringUtils.isNotEmpty(triggerGroupName)?triggerGroupName:DEFAULT_TRIGGER_GROUP_NAME);
            cronTrigger.setCronExpression(time);
            /**添加局部job监听器*/
            jobDetail.addJobListener(jobName+JOB_LISTENER_SUFFIX);
            scheduler.addJobListener(new QuartzJobListener(jobName+JOB_LISTENER_SUFFIX));
            /**添加局部tritgger监听器*/
            cronTrigger.addTriggerListener(jobName+TRIGGER_LISTENER_SUFFIX);
            scheduler.addTriggerListener(new QuartzTriggerListener(jobName+TRIGGER_LISTENER_SUFFIX));
            scheduler.scheduleJob(jobDetail,cronTrigger);
            map.put(schedulerId,scheduler);
        } catch (Exception e){
            log.info("添加定时任务抛出异常:{}",e.getMessage());
        }
    }

    /**
     * 修改定时任务触发时间
     * @param schedulerId       任务ID 必须唯一 通过此ID开启任务等操作
     * @param jobName           任务名称
     * @param jobGroupName      任务组名
     * @param triggerGroupName  触发器组名
     * @param time
     */
    public final static void updateJobTime(String schedulerId,String jobName,String jobGroupName,String triggerGroupName,String time){
        try{
            log.info("更新定时任务[{}]触发时间[{}]",jobName,time);
            if (StringUtils.isNotEmpty(map)){
                Scheduler scheduler = map.get(schedulerId);
                CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(jobName,StringUtils.isNotEmpty(triggerGroupName)?triggerGroupName:DEFAULT_TRIGGER_GROUP_NAME);
                if (StringUtils.isEmpty(cronTrigger)){
                    log.info("未找到[{}]定时任务",jobName);
                }
                String oldTime = cronTrigger.getCronExpression();
                log.info("旧触发时间:{}",oldTime);
                if (!oldTime.equalsIgnoreCase(time)){
                    log.info("新触发时间:{}",time);
                    JobDetail jobDetail = scheduler.getJobDetail(jobName,StringUtils.isNotEmpty(jobGroupName)?jobGroupName:DEFAULT_JOB_GROUP_NAME);
                    Class cls = jobDetail.getJobClass();
                    removeJob(schedulerId,jobName,jobGroupName,triggerGroupName);
                    addJob(schedulerId,jobName,jobGroupName,triggerGroupName,cls,time);
                    start(schedulerId);
                }
            }else{
                log.info("定时任务存储容器中暂无数据...");
            }
        } catch (Exception e){
            log.info("添加定时任务抛出异常:{}",e.getMessage());
        }
    }

    /**
     * 删除定时任务
     * @param schedulerId       任务ID 必须唯一 通过此ID开启任务等操作
     * @param jobName           任务名称
     * @param jobGroupName      任务组名
     * @param triggerGroupName  触发器组名
     */
    public final static void removeJob(String schedulerId,String jobName,String jobGroupName,String triggerGroupName){
        try{
            if (StringUtils.isNotEmpty(map)){
                Scheduler scheduler = map.get(schedulerId);
                //停止触发器
                scheduler.pauseTrigger(jobName,StringUtils.isNotEmpty(triggerGroupName)?triggerGroupName:DEFAULT_TRIGGER_GROUP_NAME);
                //移除触发器
                scheduler.unscheduleJob(jobName,StringUtils.isNotEmpty(triggerGroupName)?triggerGroupName:DEFAULT_TRIGGER_GROUP_NAME);
                //删除定时任务
                scheduler.deleteJob(jobName,jobGroupName);
                map.remove(schedulerId);
            }else{
                log.info("定时任务存储容器中暂无数据...");
            }
        } catch (Exception e){
            log.info("添加定时任务抛出异常:{}",e.getMessage());
        }
    }

    /**
     * 启动指定定时任务
     *
     * @description:前提：关闭或者未启动(isShutdown)或者非暂停(isInStandbyMode)
     * @param schedulerId 任务ID 必须唯一 通过此ID开启任务等操作
     * @throws SchedulerException
     */
    public static void start(String schedulerId) throws SchedulerException {
        if (StringUtils.isNotEmpty(map)){
            Scheduler scheduler = map.get(schedulerId);
            log.info("启动前状态：");
            log.info("isStarted: {}",scheduler.isStarted());
            log.info("isShutdown: {}",scheduler.isShutdown());
            log.info("isInStandbyMode: {}",scheduler.isInStandbyMode());
            if (!scheduler.isStarted()&&!scheduler.isShutdown()&&scheduler.isInStandbyMode()){
                scheduler.start();
            }
            log.info("启动后状态：");
            log.info("isStarted: {}",scheduler.isStarted());
            log.info("isShutdown: {}",scheduler.isShutdown());
            log.info("isInStandbyMode: {}",scheduler.isInStandbyMode());
        }
    }

    /**
     * 启动所有定时任务
     *
     * @description:前提：关闭或者未启动(isShutdown)或者非暂停(isInStandbyMode)
     */
    public final static void startAll() throws SchedulerException {
        if (StringUtils.isNotEmpty(map)){
            Collection<Scheduler> schedulers = map.values();
            for (Scheduler scheduler : schedulers) {
                if (!scheduler.isStarted()&&!scheduler.isShutdown()&&scheduler.isInStandbyMode()){
                    scheduler.start();
                }
            }
        }
    }

    /**
     * 关闭指定定时任务
     * 注意：关闭后的任务将永久不可用，且不可重启
     * @description:前提：已启动任务
     * @param schedulerId
     * @throws SchedulerException
     */
    public final static void shutdown(String schedulerId) throws SchedulerException {
        if (StringUtils.isNotEmpty(map)){
            Scheduler scheduler = map.get(schedulerId);
            if (scheduler.isStarted()&&!scheduler.isShutdown()){
                scheduler.shutdown();
            }
        }
    }

    /**
     * 关闭所有定时任务
     * 注意：关闭后的任务将永久不可用，且不可重启
     * @description:前提：已启动任务
     * @throws SchedulerException
     */
    public final static void shutdownAll() throws SchedulerException {
        if (StringUtils.isNotEmpty(map)){
            Collection<Scheduler> schedulers = map.values();
            for (Scheduler scheduler : schedulers) {
                if (scheduler.isStarted()&&!scheduler.isShutdown()){
                    scheduler.shutdown();
                }
            }
        }
    }

    /**
     * 临时暂停指定定时任务
     * @param schedulerId 任务ID 必须唯一 通过此ID开启任务等操作
     * @description:前提：已启动后任务
     * @throws SchedulerException
     */
    public final static void stop(String schedulerId,String jobName,String triggerGroupName,String jobGroupName) throws SchedulerException {
        if (StringUtils.isNotEmpty(map)){
            Scheduler scheduler = map.get(schedulerId);
            if (scheduler.isStarted()&&!scheduler.isShutdown()){
                scheduler.pauseJob(jobName,StringUtils.isNotEmpty(jobGroupName)?jobGroupName:DEFAULT_JOB_GROUP_NAME);
                scheduler.pauseTrigger(jobName,StringUtils.isNotEmpty(triggerGroupName)?triggerGroupName:DEFAULT_TRIGGER_GROUP_NAME);
            }
        }
    }

    /**
     * 临时暂停所有定时任务
     *
     * @description:前提：已启动后任务
     */
    public final static void stopAll() throws SchedulerException {
        if (StringUtils.isNotEmpty(map)){
            Collection<Scheduler> schedulers = map.values();
            for (Scheduler scheduler : schedulers) {
                if (scheduler.isStarted()&&!scheduler.isShutdown()){
                    scheduler.pauseAll();
                }
            }
        }
    }

    /**
     * 重启指定定时任务
     * @param schedulerId 任务ID 必须唯一 通过此ID开启任务等操作
     *
     * @description:前提：已暂停后任务
     */
    public final static void resume(String schedulerId,String jobName,String triggerGroupName,String jobGroupName) throws SchedulerException {
        if (StringUtils.isNotEmpty(map)){
            Scheduler scheduler = map.get(schedulerId);
            if (scheduler.isStarted()&&!scheduler.isInStandbyMode()){
                //暂停后重启
                scheduler.resumeJob(jobName,StringUtils.isNotEmpty(jobGroupName)?jobGroupName:DEFAULT_JOB_GROUP_NAME);
                scheduler.resumeTrigger(jobName,StringUtils.isNotEmpty(triggerGroupName)?triggerGroupName:DEFAULT_TRIGGER_GROUP_NAME);
            }
        }
    }

    /**
     * 重启所有定时任务
     *
     * @description:前提：已暂停后任务
     */
    public final static void resumeAll() throws SchedulerException {
        if (StringUtils.isNotEmpty(map)){
            Collection<Scheduler> schedulers = map.values();
            for (Scheduler scheduler : schedulers) {
                if (scheduler.isStarted()&&!scheduler.isInStandbyMode()){
                    //暂停后重启
                    scheduler.resumeAll();
                }
            }
        }
    }
}
