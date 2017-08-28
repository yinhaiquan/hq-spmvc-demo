package hq.com;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @title : 测试定时任务
 * @describle :
 *          测试quartz获取bean例子，正式环境下可以将QuartzDemo.class写在web子项目，引入datacenter中ctx的工具类，这里只做演示
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/8 17:43 星期二
 */
@Deprecated
public class QuartzDemo2 implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        /**测试quartz中获取spring bean方式*/
//        try {
//            JedisTemplate jedisTemplate = (JedisTemplate) SpringApplicationContext.getBean("jedisTemplate");
//            System.out.println(jedisTemplate.getResource().info());
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+ "★★★★★★★★★★★");
    }
}
