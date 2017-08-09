package hq.com;

import hq.com.quartz.QuartzDemo;
import hq.com.quartz.QuartzManager;
import hq.com.redis.client.RedisClientTemplate;
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

    @Test
    public void testFind() throws SchedulerException, InterruptedException {
//       Jedis jedis = (Jedis) jedisTemplate.getResource();
//        System.out.println(jedis.info());
        QuartzManager.addJob("123","test",null,null, QuartzDemo.class,"0/1 * * * * ?");
        QuartzManager.addJob("123456","test2",null,null, QuartzDemo.class,"0/1 * * * * ?");
        System.out.println("3秒后开启所有定时任务");
        Thread.sleep(3*1000);
        QuartzManager.startAll();
        System.out.println("3秒后关闭所有定时任务");
        Thread.sleep(3*1000);
        QuartzManager.stopAll();
//        System.out.println("3秒后开启一个定时任务");
//        Thread.sleep(3*1000);
//        QuartzManager.start("123");
//        QuartzManager.start("123456");
        /*测试关闭定时任务*/
//        Thread.sleep(3*1000);
//        System.out.println("3秒后关闭定时任务");
//        QuartzManager.stop("123","test",null,null);
//        System.out.println("3秒后重新启动定时任务");
//        Thread.sleep(3*1000);
//        QuartzManager.resume("123","test",null,null);

        System.out.println("3秒后重新启动所有定时任务");
        Thread.sleep(3*1000);
        QuartzManager.resumeAll();

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
