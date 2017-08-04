package hq.com;

import hq.com.aop.concurrency.AsyncEvent;
import hq.com.aop.concurrency.impl.AsyncEventSourceImpl;
import hq.com.aop.concurrency.AsyncHandlerListener;
import hq.com.aop.concurrency.AsyncObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @title : 适用高并发下新增更新操作
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/3 16:12 星期一
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring.xml"})
public class QueueThreadTest {

    @Resource(name = "asyncEventSource")
    private AsyncEventSourceImpl asyncEventSource;


    @Test
    public void testThreads() throws InterruptedException {
        AsyncHandlerListener asyncHandlerListener = new AsyncHandlerListener() {
            @Override
            public void eventHandler(AsyncEvent asyncEvent) {
                System.out.println(asyncEvent.getParams());
            }
        };
        AsyncObject asyncObject_1 = new AsyncObject();
        asyncObject_1.setAsyncHandlerListener(asyncHandlerListener);
        Map<String, Object> param_1 = new HashMap<>();
        param_1.put("name", "fuck");
        param_1.put("age", "12");
        asyncObject_1.setParams(param_1);

        AsyncObject asyncObject_2 = new AsyncObject();
        asyncObject_2.setAsyncHandlerListener(asyncHandlerListener);
        Map<String, Object> param_2 = new HashMap<>();
        param_2.put("name", "fuck123");
        param_2.put("age", "12123");
        asyncObject_2.setParams(param_2);

        asyncEventSource.addListener(asyncObject_1);
        asyncEventSource.addListener(asyncObject_2);
        asyncEventSource.signalAll();
        Thread.sleep(30 * 1000);
    }

}
