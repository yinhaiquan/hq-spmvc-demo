package hq.com.quartz.base;

import hq.com.aop.utils.StringUtils;
import hq.com.quartz.QuartzManager;
import hq.com.quartz.vo.SchedulerInfo;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @title : 属性文件工具类
 * @describle : 任务属性记录保存读取操作
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/10 10:41 星期四
 */
public class PropertyConfigHelper {
    private static Logger log = LoggerFactory.getLogger(PropertyConfigHelper.class);
    /**用于定时任务schedulerId列表存储*/
    public static final String KEY_PREFIX = "scheduler.key";
    public static final String JOB_NAME_SUFFIX = ".jobName";
    public static final String JOB_CLASS_SUFFIX = ".jobClass";
    public static final String JOB_GROUP_NAME_SUFFIX = ".jobGroupName";
    public static final String TRIGGER_GROUP_NAME_SUFFIX = ".triggerGroupName";
    public static final String CRON_EXPRESSION_TIME_SUFFIX = ".time";
    public static final String JOB_RUNNING_STATE_SUFFIX =".state";
    private PropertiesConfiguration configuration;
    private static PropertyConfigHelper propertyConfigHelper;

    private PropertyConfigHelper(PropertiesConfiguration configuration){
        this.configuration = configuration;
    }

    public final static PropertyConfigHelper newInstance(){
        if (StringUtils.isEmpty(propertyConfigHelper)){
            try {
                StringBuffer path = new StringBuffer("file:///G:\\hq-spmvc-demo\\project\\war\\src\\main\\resources\\");
                path.append("schedulerDB.properties");
                log.info("任务存储文件位置:{}",path.toString());
                PropertiesConfiguration configuration = new PropertiesConfiguration(path.toString());
//            configuration = new PropertiesConfiguration("schedulerDB.properties");
                configuration.setEncoding("utf-8");
                //设置是否自动提交，true自动提交，false 则需要configuration.save()保存提交。
                configuration.setAutoSave(true);
                propertyConfigHelper = new PropertyConfigHelper(configuration);
            } catch (ConfigurationException e) {
                log.info("读取属性文件异常:{}",e.getMessage());
            }
        }
        return propertyConfigHelper;
    }

    /**
     * 获取key对应属性值
     * @param key
     * @return
     */
    public final String getValue(String key) {
        return configuration.getString(key);
    }

    /**
     * 获取任务属性实体记录信息
     * @param schedulerId 任务ID
     * @return
     * @throws ClassNotFoundException
     */
    public final SchedulerInfo getSchedulerInfo(String schedulerId) throws ClassNotFoundException {
        SchedulerInfo si = new SchedulerInfo();
        si.setSchedulerId(schedulerId);
        si.setJobName(getValue(schedulerId+JOB_NAME_SUFFIX));
        si.setJobClass(Class.forName(getValue(schedulerId+JOB_CLASS_SUFFIX)));
        si.setJobGroupName(getValue(schedulerId+JOB_GROUP_NAME_SUFFIX));
        si.setTriggerGroupName(getValue(schedulerId+TRIGGER_GROUP_NAME_SUFFIX));
        si.setTime(getValue(schedulerId+CRON_EXPRESSION_TIME_SUFFIX));
        si.setStatusEnum(StatusEnum.newInstance(getValue(schedulerId+JOB_RUNNING_STATE_SUFFIX)));
        return si;
    }

    /**
     * 根据前缀获取key列表
     * @param prefix 前缀
     * @return
     */
    public final List<String> getKeys(String prefix){
        List<String> keys = new ArrayList<>();
        System.out.println(configuration);
        Iterator iterator = configuration.getKeys(prefix);
        while (iterator.hasNext()){
            keys.add(String.valueOf(iterator.next()).replaceAll(KEY_PREFIX+".",""));
        }
        return keys;
    }

    /**
     * 存储任务属性记录，新增任务时默认初始化中，任务并未开启
     * <p>
     *     <b>note:</b>
     *          initialing  初始化中
     *          running     运行中
     *          pausing     暂停中
     *          shutdowning 关闭中
     * </p>
     * @param jobName          任务名称
     * @param jobClass         任务class
     * @param jobGroupName     任务组名
     * @param triggerGroupName 触发器组名
     * @param time             触发时间
     * @throws ConfigurationException
     */
    public final synchronized String addJobProp(String jobName,
                                        String jobClass,
                                        String jobGroupName,
                                        String triggerGroupName,
                                        String time) throws ConfigurationException {
        String schedulerId = UUID.randomUUID().toString().replaceAll("-","");
        configuration.setProperty(KEY_PREFIX+"."+schedulerId,schedulerId);
        configuration.setProperty(schedulerId+JOB_NAME_SUFFIX,jobName);
        configuration.setProperty(schedulerId+JOB_CLASS_SUFFIX,jobClass);
        configuration.setProperty(schedulerId+JOB_GROUP_NAME_SUFFIX, StringUtils.isNotEmpty(jobGroupName)?jobGroupName: QuartzManager.DEFAULT_JOB_GROUP_NAME);
        configuration.setProperty(schedulerId+TRIGGER_GROUP_NAME_SUFFIX,StringUtils.isNotEmpty(triggerGroupName)?triggerGroupName:QuartzManager.DEFAULT_TRIGGER_GROUP_NAME);
        configuration.setProperty(schedulerId+CRON_EXPRESSION_TIME_SUFFIX,time);
        configuration.setProperty(schedulerId+JOB_RUNNING_STATE_SUFFIX,"initialize");
        return schedulerId;
    }

    /**
     * 修改记录
     * @param key
     * @param value
     */
    public final synchronized void updateJobProp(String key,String value) throws ConfigurationException {
        configuration.setProperty(key,value);
    }

    /**
     * 移除任务记录
     * @param schedulerId 任务ID UUID生成
     */
    public final synchronized void removeJobProp(String schedulerId){
        configuration.clearProperty(KEY_PREFIX+"."+schedulerId);
        configuration.clearProperty(schedulerId+JOB_NAME_SUFFIX);
        configuration.clearProperty(schedulerId+JOB_CLASS_SUFFIX);
        configuration.clearProperty(schedulerId+JOB_GROUP_NAME_SUFFIX);
        configuration.clearProperty(schedulerId+TRIGGER_GROUP_NAME_SUFFIX);
        configuration.clearProperty(schedulerId+CRON_EXPRESSION_TIME_SUFFIX);
    }

    public static void main(String[] args) throws ConfigurationException, ClassNotFoundException {
//        PropertyConfigHelper.newInstance().addJobProp("test",
//                   "hq.com.quartz.QuartzDemo",
//                   null,
//                   null,
//                   "0/1 * * * * ?");
        List<String> list = PropertyConfigHelper.newInstance().getKeys(KEY_PREFIX);
//        PropertyConfigHelper.newInstance().updateJobProp("2a2ca7e81cc84a4aa9ab48f6b670d4f0.time","00000");
//        PropertyConfigHelper.newInstance().removeJobProp("a1be24095ce04bfea86f60b190c9a349");
        for (String object:
             list) {
            System.out.println(object);
        }
//        System.out.println(PropertyConfigHelper.newInstance().getSchedulerInfo("56cfc37bf21b46f08cd6b9906945d8a8"));
    }
}
