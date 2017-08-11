package hq.com.quartz.service;

import hq.com.aop.annotation.Log;
import hq.com.aop.vo.OutParam;
import hq.com.exception.IllegalOptionException;
import hq.com.quartz.vo.QuartzInParam;

/**
 * @title : quartz任务集成业务层
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/10 17:15 星期四
 */
public interface QuartzService {
    /**
     * 新增任务
     * @param quartzInParam 任务属性
     * @return
     */
    @Log(desc = "新增任务参数:{0}")
    public OutParam addJob(QuartzInParam quartzInParam) throws IllegalOptionException;

    /**
     * 修改任务触发时间
     * @param quartzInParam 任务属性
     * @return
     */
    @Log(desc = "修改任务触发时间参数:{0}")
    public OutParam updateJobTime(QuartzInParam quartzInParam) throws IllegalOptionException;

    /**
     * 删除任务
     * @param quartzInParam 任务属性
     * @return
     */
    @Log(desc = "删除任务参数:{0}")
    public OutParam deleteJob(QuartzInParam quartzInParam) throws IllegalOptionException;

    /**
     * 任务开关
     * @param quartzInParam
     * @return
     */
    @Log(desc = "任务开关参数:{0}")
    public OutParam switchJob(QuartzInParam quartzInParam) throws IllegalOptionException;

    /**
     * 分页获取任务列表
     * @param quartzInParam
     * @return
     */
    @Log(desc = "分页获取任务列表参数:{0}")
    public OutParam getSchedulerList(QuartzInParam quartzInParam) throws IllegalOptionException;
}
