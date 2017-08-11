package hq.com.quartz.service.impl;

import hq.com.aop.vo.OutParam;
import hq.com.aop.vo.Pager;
import hq.com.enums.SystemCodeEnum;
import hq.com.exception.IllegalOptionException;
import hq.com.quartz.base.SwitchTypeEnum;
import hq.com.quartz.dao.QuartzDao;
import hq.com.quartz.service.QuartzService;
import hq.com.quartz.vo.QuartzInParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @title : quartz任务集成业务实现层
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/10 17:33 星期四
 */
@Service("quartzSvc")
public class QuartzServiceImpl implements QuartzService{
    private static Logger log = LoggerFactory.getLogger(QuartzServiceImpl.class);
    @Resource(name = "quartzDao")
    private QuartzDao quartzDao;

    /**
     * 新增任务
     *
     * @param quartzInParam 任务属性
     * @return
     */
    @Override
    public OutParam addJob(QuartzInParam quartzInParam) throws IllegalOptionException {
        OutParam op = new OutParam();
        try{
            quartzDao.addJob(quartzInParam.getJobName(),
                    quartzInParam.getJobClass(),
                    quartzInParam.getJobGroupName(),
                    quartzInParam.getTriggerGroupName(),
                    quartzInParam.getTime());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("新增任务【addJob】抛出异常:{}", e.getMessage());
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_ERROR);
        }
        return op;
    }

    /**
     * 修改任务触发时间
     *
     * @param quartzInParam 任务属性
     * @return
     */
    @Override
    public OutParam updateJobTime(QuartzInParam quartzInParam) throws IllegalOptionException {
        OutParam op = new OutParam();
        try{
            quartzDao.updateJobTime(quartzInParam.getSchedulerId(),quartzInParam.getTime());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("修改任务触发时间【updateJobTime】抛出异常:{}", e.getMessage());
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_ERROR);
        }
        return op;
    }

    /**
     * 删除任务
     *
     * @param quartzInParam 任务属性
     * @return
     */
    @Override
    public OutParam deleteJob(QuartzInParam quartzInParam) throws IllegalOptionException {
        OutParam op = new OutParam();
        try{
            quartzDao.deleteJob(quartzInParam.getSchedulerId());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("删除任务【deleteJob】抛出异常:{}", e.getMessage());
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_ERROR);
        }
        return op;
    }

    /**
     * 任务开关
     *
     * @param quartzInParam
     * @return
     */
    @Override
    public OutParam switchJob(QuartzInParam quartzInParam) throws IllegalOptionException {
        OutParam op = new OutParam();
        try{
            quartzDao.switchJob(quartzInParam.getSchedulerId(),
                   SwitchTypeEnum.newInstance(quartzInParam.getSwitchType()));
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("任务开关【switchJob】抛出异常:{}", e.getMessage());
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_ERROR);
        }
        return op;
    }

    /**
     * 分页获取任务列表
     *
     * @param quartzInParam
     * @return
     */
    @Override
    public OutParam getSchedulerList(QuartzInParam quartzInParam) throws IllegalOptionException {
        OutParam op = new OutParam();
        try{
            Pager pager = quartzDao.getSchedulerList(quartzInParam.getPage(),quartzInParam.getPageSize());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
            op.setContent(pager);
        } catch (Exception e) {
            log.info("分页获取任务列表【getSchedulerList】抛出异常:{}", e.getMessage());
            throw new IllegalOptionException(SystemCodeEnum.SYSTEM_ERROR);
        }
        return op;
    }
}
