package hq.com;

import hq.com.quartz.QuartzManager;
import hq.com.quartz.base.SwitchTypeEnum;
import hq.com.quartz.dao.QuartzDao;
import hq.com.redis.client.RedisClientTemplate;
import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/8/8 17:50 星期二
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-redis.xml"})
public class QuartzTest {

    @Resource(name = "jedisTemplate")
    private RedisClientTemplate jedisTemplate;

    @Resource(name = "quartzDao")
    private QuartzDao quartzDao;

    @Test
    public void testQuartzDao() throws InterruptedException, ClassNotFoundException, ConfigurationException, SchedulerException {
        System.out.println("测试任务容器启动");
//        System.out.println("测试4秒后新增二个任务");
//        Thread.sleep(4*1000);
//        quartzDao.addJob("111333",
//                "hq.com.quartz.QuartzDemo",
//                null,
//                null,
//                "0/1 * * * * ?");
//        quartzDao.addJob("11133323",
//                "hq.com.quartz.QuartzDemo",
//                null,
//                null,
//                "0/1 * * * * ?");
//        System.out.println("**********************************************");
//        System.out.println(quartzDao.getSchedulerList(1,10));
        System.out.println("5秒后暂停所有任务");
        Thread.sleep(5*1000);

        quartzDao.switchJob(null, SwitchTypeEnum.STOPALL);
        Thread.sleep(60*1000);
    }

    @Test
    public void testFind() throws SchedulerException, InterruptedException {
//       Jedis jedis = (Jedis) jedisTemplate.getResource();
//        System.out.println(jedis.info());
        QuartzManager.addJob("123","test",null,null, hq.com.quartz.QuartzDemo.class,"0/1 * * * * ?");
        QuartzManager.addJob("123456","test2",null,null, hq.com.quartz.QuartzDemo.class,"0/1 * * * * ?");
        System.out.println("3秒后开启所有定时任务");
        Thread.sleep(3*1000);
        QuartzManager.startAll();
//        System.out.println("3秒后暂停所有定时任务");
//        Thread.sleep(3*1000);
//        QuartzManager.stopAll();
//        System.out.println("3秒后开启定时任务");
//        Thread.sleep(3*1000);
//        QuartzManager.start("123");
//        QuartzManager.start("123456");
        /*测试关闭定时任务*/
        Thread.sleep(3*1000);
        System.out.println("3秒后关闭所有定时任务");
        QuartzManager.shutdown("123");
        QuartzManager.removeJob("123","test",null,null);
//            QuartzManager.shutdownAll();
//        System.out.println("3秒后暂停定时任务");
//        QuartzManager.stop("123","test",null,null);
//        System.out.println("3秒后重新开启定时任务");
//        Thread.sleep(3*1000);
//        QuartzManager.start("123");

//        System.out.println("3秒后重新启动定时任务");
//        Thread.sleep(3*1000);
//        QuartzManager.resume("123","test",null,null);

//        System.out.println("3秒后重新启动所有定时任务");
//        Thread.sleep(3*1000);
//        QuartzManager.resumeAll();

        /*测试修改定时任务触发时间*/
//        QuartzManager.updateJobTime("123","test",null,null,"0/2 * * * * ?");
        /*测试删除定时任务 start*/
//        Thread.sleep(5*1000);
//        QuartzManager.removeJob("123","test",null,null);
//        System.out.println("已删除定时任务");
        /*测试删除定时任务 end*/
        Thread.sleep(60*1000);
    }
}
