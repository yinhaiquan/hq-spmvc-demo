package hq.com.quartz.dao.impl;

import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.Pager;
import hq.com.quartz.QuartzManager;
import hq.com.quartz.base.PropertyConfigHelper;
import hq.com.quartz.base.StatusEnum;
import hq.com.quartz.base.SwitchTypeEnum;
import hq.com.quartz.dao.QuartzDao;
import hq.com.quartz.vo.SchedulerInfo;
import org.apache.commons.configuration.ConfigurationException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @title : 任务对外业务实现层
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/10 15:14 星期四
 */
public class QuartzDaoImpl implements QuartzDao {
    private static Logger log = LoggerFactory.getLogger(QuartzDaoImpl.class);
    /**
     * 新增任务
     *
     * @param jobName          任务名称
     * @param jobClass         任务Class
     * @param jobGroupName     任务组名
     * @param triggerGroupName 触发器组名
     * @param time             触发时间
     */
    @Override
    public void addJob(String jobName, String jobClass, String jobGroupName, String triggerGroupName, String time) throws ConfigurationException, ClassNotFoundException {
        String schedulerId = PropertyConfigHelper.newInstance().addJobProp(jobName,jobClass,jobGroupName,triggerGroupName,time);
        Class cls = Class.forName(jobClass);
        QuartzManager.addJob(schedulerId,jobName,jobGroupName,triggerGroupName,cls,time);
    }

    /**
     * 修改任务触发时间
     *
     * @param schedulerId 任务ID
     * @param newTime     新触发时间
     */
    @Override
    public void updateJobTime(String schedulerId, String newTime) throws ClassNotFoundException, ConfigurationException {
        PropertyConfigHelper.newInstance().updateJobProp(schedulerId+PropertyConfigHelper.CRON_EXPRESSION_TIME_SUFFIX,newTime);
        PropertyConfigHelper.newInstance().updateJobProp(schedulerId+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,"running");
        SchedulerInfo schedulerInfo = PropertyConfigHelper.newInstance().getSchedulerInfo(schedulerId);
        if (StringUtils.isNotEmpty(schedulerInfo)){
            QuartzManager.updateJobTime(schedulerId,
                    schedulerInfo.getJobName(),
                    schedulerInfo.getJobGroupName(),
                    schedulerInfo.getTriggerGroupName(),
                    newTime);
        } else {
            log.info("任务容器中不存在该任务[{}],请检查schedulerDB.properties文件",schedulerId);
        }
    }

    /**
     * 删除任务
     *
     * @param schedulerId 任务ID
     */
    @Override
    public void deleteJob(String schedulerId) throws ClassNotFoundException {
        SchedulerInfo schedulerInfo = PropertyConfigHelper.newInstance().getSchedulerInfo(schedulerId);
        PropertyConfigHelper.newInstance().removeJobProp(schedulerId);
        if (StringUtils.isNotEmpty(schedulerInfo)){
            QuartzManager.removeJob(schedulerId,
                    schedulerInfo.getJobName(),
                    schedulerInfo.getJobGroupName(),
                    schedulerInfo.getTriggerGroupName());
        } else {
            log.info("任务容器中不存在该任务[{}],请检查schedulerDB.properties文件",schedulerId);
        }
    }

    /**
     * 任务开关
     *
     * @param schedulerId    任务ID
     * @param switchTypeEnum 开关类型
     */
    @Override
    public void switchJob(String schedulerId, SwitchTypeEnum switchTypeEnum) throws SchedulerException, ClassNotFoundException, ConfigurationException {
        switch (switchTypeEnum){
            case START:
                QuartzManager.start(schedulerId);
                PropertyConfigHelper.newInstance().updateJobProp(
                        schedulerId+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                        StatusEnum.RUNNING.getCode());
                break;
            case STARTALL:
                QuartzManager.startAll();
                List<String> list = PropertyConfigHelper.newInstance().getKeys(PropertyConfigHelper.KEY_PREFIX);
                for (String key : list) {
                    PropertyConfigHelper.newInstance().updateJobProp(
                            key+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                            StatusEnum.RUNNING.getCode());
                }
                break;
            case STOP:
                SchedulerInfo schedulerInfo = PropertyConfigHelper.newInstance().getSchedulerInfo(schedulerId);
                if (StringUtils.isNotEmpty(schedulerInfo)){
                    QuartzManager.stop(schedulerInfo.getSchedulerId(),
                            schedulerInfo.getJobName(),
                            schedulerInfo.getJobGroupName(),
                            schedulerInfo.getTriggerGroupName());
                    PropertyConfigHelper.newInstance().updateJobProp(
                            schedulerId+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                            StatusEnum.PAUSING.getCode());
                } else {
                    log.info("任务容器中不存在该任务[{}],请检查schedulerDB.properties文件",schedulerId);
                }
                break;
            case STOPALL:
                QuartzManager.stopAll();
                List<String> l = PropertyConfigHelper.newInstance().getKeys(PropertyConfigHelper.KEY_PREFIX);
                for (String key : l) {
                    PropertyConfigHelper.newInstance().updateJobProp(
                            key+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                            StatusEnum.PAUSING.getCode());
                }
                break;
            case RESUME:
                SchedulerInfo s = PropertyConfigHelper.newInstance().getSchedulerInfo(schedulerId);
                if (StringUtils.isNotEmpty(s)){
                    QuartzManager.resume(s.getSchedulerId(),
                            s.getJobName(),
                            s.getJobGroupName(),
                            s.getTriggerGroupName());
                    PropertyConfigHelper.newInstance().updateJobProp(
                            schedulerId+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                            StatusEnum.RUNNING.getCode());
                } else {
                    log.info("任务容器中不存在该任务[{}],请检查schedulerDB.properties文件",schedulerId);
                }
                break;
            case RESUMEALL:
                QuartzManager.resumeAll();
                List<String> l2 = PropertyConfigHelper.newInstance().getKeys(PropertyConfigHelper.KEY_PREFIX);
                for (String key : l2) {
                    PropertyConfigHelper.newInstance().updateJobProp(
                            key+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                            StatusEnum.RUNNING.getCode());
                }
                break;
            case SHUTDOWN:
                QuartzManager.shutdown(schedulerId);
                PropertyConfigHelper.newInstance().updateJobProp(
                        schedulerId+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                        StatusEnum.SHUTDOWNING.getCode());
                break;
            case SHUTDOWNALL:
                QuartzManager.shutdownAll();
                List<String> l3 = PropertyConfigHelper.newInstance().getKeys(PropertyConfigHelper.KEY_PREFIX);
                for (String key : l3) {
                    PropertyConfigHelper.newInstance().updateJobProp(
                            key+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                            StatusEnum.SHUTDOWNING.getCode());
                }
                break;
        }
    }

    /**
     * 分页获取任务列表
     *
     * @param page     页码
     * @param pageSize 页面大小
     * @return
     */
    @Override
    public Pager getSchedulerList(int page, int pageSize){
        Pager pager = new Pager();
        List<SchedulerInfo> infoList = new ArrayList<>();
        List<String> list = PropertyConfigHelper.newInstance().getKeys(PropertyConfigHelper.KEY_PREFIX);
        int pages = 0;
        if (StringUtils.isNotEmpty(list)){
            //伪分页实现，由于容器中任务数量不多，故伪分页不影响系统执行效率，可忽略不计
            if (list.size()%pageSize==0){
                pages = list.size()/pageSize;
            }else{
                pages = 1+(list.size()/pageSize);
            }
            int index = (page-1)*pageSize;
            int pageTotal = index+pageSize;
            if(index<list.size()){
                for (int i = index; i <(pageTotal<list.size()?pageTotal:list.size()) ; i++) {
                    try {
                        SchedulerInfo schedulerInfo = PropertyConfigHelper.newInstance().getSchedulerInfo(list.get(i));
                        Scheduler scheduler = QuartzManager.map.get(list.get(i));
                        if (StringUtils.isNotEmpty(scheduler)){
                            try {
                                schedulerInfo.setStarted(scheduler.isStarted());
                                schedulerInfo.setShutdown(scheduler.isShutdown());
                                schedulerInfo.setInStandbyMode(scheduler.isInStandbyMode());
                            } catch (SchedulerException e) {
                                log.info("实时获取任务状态异常：{}",e.getMessage());
                            }
                        }
                        infoList.add(schedulerInfo);
                    } catch (ClassNotFoundException e) {
                        log.info("任务[{}]class类异常:{}",list.get(i),e.getMessage());
                    }
                }
            }
        }
        pager.setRows(infoList);
        pager.setPageSize(pageSize);
        pager.setTotal(list.size());
        pager.setPages(pages);
        return pager;
    }

    /**
     * 获取容器所有任务
     *
     * @return
     */
    @Override
    public List<SchedulerInfo> getAll(){
        List<SchedulerInfo> infoList = new ArrayList<>();
        List<String> list = PropertyConfigHelper.newInstance().getKeys(PropertyConfigHelper.KEY_PREFIX);
        if (StringUtils.isNotEmpty(list)){
            for (String key : list) {
                try {
                    SchedulerInfo schedulerInfo = PropertyConfigHelper.newInstance().getSchedulerInfo(key);
                    infoList.add(schedulerInfo);
                } catch (ClassNotFoundException e) {
                    log.info("任务[{}]class类异常:{}",key,e.getMessage());
                }
            }
        }
        return infoList;
    }

    /**
     * 初始化任务容器
     *
     * 项目重启时随着spring容器bean的初始化时，初始化任务容器
     */
    @PostConstruct
    public void init() throws ClassNotFoundException, SchedulerException, ConfigurationException {
        log.info("任务容器启动中...");
        List<String> list = PropertyConfigHelper.newInstance().getKeys(PropertyConfigHelper.KEY_PREFIX);
        if (StringUtils.isNotEmpty(list)){
            log.info("任务容器中一共加载{}个任务,任务信息如下：",list.size());
            for (String key : list) {
                try {
                    SchedulerInfo schedulerInfo = PropertyConfigHelper.newInstance().getSchedulerInfo(key);
                    log.info("任务：{}",schedulerInfo);
                    QuartzManager.addJob(schedulerInfo.getSchedulerId(),
                            schedulerInfo.getJobName(),
                            schedulerInfo.getJobGroupName(),
                            schedulerInfo.getTriggerGroupName(),
                            schedulerInfo.getJobClass(),
                            schedulerInfo.getTime());
                } catch (ClassNotFoundException e) {
                    PropertyConfigHelper.newInstance().updateJobProp(
                            key+PropertyConfigHelper.JOB_RUNNING_STATE_SUFFIX,
                            StatusEnum.FAILURING.getCode());
                    log.info("任务容器加载[{}]任务失败,请检查schedulerDB.properties文件",key);
                }
            }
            log.info("任务容器启动完成!");
            //启动所有任务
            switchJob(null,SwitchTypeEnum.STARTALL);
        }else{
            log.info("任务容器中暂无任务...");
        }
    }
}
