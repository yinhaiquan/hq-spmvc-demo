package hq.com.test;

import hq.com.demo.App;
import hq.com.aop.ctx.SpringApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Unit test for simple App.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/*.xml"})
public class AppTest {


    @Test
    public void testSpring() throws ClassNotFoundException {

        App obj = (App) SpringApplicationContext.getBean("appSvc");
        System.out.println(SpringApplicationContext.getBean("appSvc").getClass().getName());
        System.out.println(SpringApplicationContext.getBean("appSvc").getClass().getSimpleName());
        System.out.println(SpringApplicationContext.getBean("appSvc").getClass().getPackage().getName());

        System.out.println(obj.say("123"));
    }

}
