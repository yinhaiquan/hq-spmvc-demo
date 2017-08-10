package hq.com.quartz.dao;

import hq.com.aop.vo.Pager;
import hq.com.quartz.base.SwitchTypeEnum;
import hq.com.quartz.vo.SchedulerInfo;
import org.apache.commons.configuration.ConfigurationException;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @title : 任务对外业务层
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/10 14:32 星期四
 */
public interface QuartzDao {

    /**
     * 新增任务
     * @param jobName           任务名称
     * @param jobClass          任务Class
     * @param jobGroupName      任务组名
     * @param triggerGroupName  触发器组名
     * @param time              触发时间
     */
    public void addJob(String jobName,
                    String jobClass,
                    String jobGroupName,
                    String triggerGroupName,
                    String time) throws ConfigurationException, ClassNotFoundException;

    /**
     * 修改任务触发时间
     * @param schedulerId  任务ID
     * @param newTime      新触发时间
     */
    public void updateJobTime(String schedulerId,String newTime) throws ClassNotFoundException;

    /**
     * 删除任务
     * @param schedulerId  任务ID
     */
    public void deleteJob(String schedulerId) throws ClassNotFoundException;

    /**
     * 任务开关
     * @param schedulerId      任务ID
     * @param switchTypeEnum   开关类型
     */
    public void switchJob(String schedulerId,SwitchTypeEnum switchTypeEnum) throws SchedulerException, ClassNotFoundException;

    /**
     * 分页获取任务列表
     * @param page     页码
     * @param pageSize 页面大小
     * @return
     */
    public Pager getSchedulerList(int page, int pageSize) throws ClassNotFoundException;
}
