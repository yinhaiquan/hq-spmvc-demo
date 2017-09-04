package hq.com;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @title : 测试zk配置管理装载
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/9/4 11:52 星期一
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-zk.xml"})
public class ZkTest {
    @Test
    public void testZkConfig(){
        System.out.println(123456);
    }
}
