package hq.com.quartz.service.impl;

import hq.com.aop.utils.StringUtils;
import hq.com.aop.vo.OutParam;
import hq.com.aop.vo.Pager;
import hq.com.base.BaseService;
import hq.com.enums.SystemCodeEnum;
import hq.com.exception.IllegalOptionException;
import hq.com.quartz.base.SwitchTypeEnum;
import hq.com.quartz.dao.QuartzDao;
import hq.com.quartz.service.QuartzService;
import hq.com.quartz.vo.QuartzInParam;
import hq.com.quartz.vo.SchedulerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title : quartz任务集成业务实现层
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/10 17:33 星期四
 */
@Service("quartzSvc")
public class QuartzServiceImpl extends BaseService implements QuartzService{
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
            isNull(quartzInParam);
            quartzDao.addJob(quartzInParam.getJobName(),
                    quartzInParam.getJobClass(),
                    quartzInParam.getJobGroupName(),
                    quartzInParam.getTriggerGroupName(),
                    quartzInParam.getTime());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("新增任务【addJob】抛出异常:{}", e.getMessage());
            exception(e);
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
            isNull(quartzInParam);
            quartzDao.updateJobTime(quartzInParam.getSchedulerId(),quartzInParam.getTime());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("修改任务触发时间【updateJobTime】抛出异常:{}", e.getMessage());
            exception(e);
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
            isNull(quartzInParam);
            quartzDao.deleteJob(quartzInParam.getSchedulerId());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("删除任务【deleteJob】抛出异常:{}", e.getMessage());
            exception(e);
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
            isNull(quartzInParam);
            quartzDao.switchJob(quartzInParam.getSchedulerId(),
                   SwitchTypeEnum.newInstance(quartzInParam.getSwitchType()));
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
        } catch (Exception e) {
            log.info("任务开关【switchJob】抛出异常:{}", e.getMessage());
            exception(e);
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
            isNull(quartzInParam);
            Pager pager = quartzDao.getSchedulerList(quartzInParam.getPage(),quartzInParam.getPageSize());
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
            op.setContent(pager);
        } catch (Exception e) {
            log.info("分页获取任务列表【getSchedulerList】抛出异常:{}", e.getMessage());
            exception(e);
        }
        return op;
    }

    /**
     * 监控容器中任务量
     *
     * @return
     * @throws IllegalOptionException
     */
    @Override
    public OutParam monitorContainerScheduler() throws IllegalOptionException {
        OutParam op = new OutParam();
        Map<String,Object> map = new HashMap<>();
        try{
            int total = 0;
            int initialings = 0;
            int runnings = 0;
            int pausings = 0;
            int shutdownings = 0;
            int failurings = 0;
            List<SchedulerInfo> list = quartzDao.getAll();
            if (StringUtils.isNotEmpty(list)){
                total = list.size();
                for (SchedulerInfo schedulerInfo : list) {
                    switch (schedulerInfo.getStatusEnum()){
                        case PAUSING:
                            pausings++;
                            break;
                        case RUNNING:
                            runnings++;
                            break;
                        case FAILURING:
                            failurings++;
                            break;
                        case INITIALING:
                            initialings++;
                            break;
                        case SHUTDOWNING:
                            shutdownings++;
                            break;
                    }
                }
            }
            map.put("total",total);
            map.put("initialings",initialings);
            map.put("runnings",runnings);
            map.put("pausings",pausings);
            map.put("shutdownings",shutdownings);
            map.put("failurings",failurings);
            op.setCode(SystemCodeEnum.SYSTEM_OK.getCode());
            op.setDesc(SystemCodeEnum.SYSTEM_OK.getDesc());
            op.setContent(map);
        } catch (Exception e) {
            log.info("监控容器中任务量【monitorContainerScheduler】抛出异常:{}", e.getMessage());
            exception(e);
        }
        return op;
    }
}
