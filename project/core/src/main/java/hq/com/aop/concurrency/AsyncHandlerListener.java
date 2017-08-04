package hq.com.aop.concurrency;

import java.util.EventListener;

/**
 * @title : 事件监听器
 * @describle : 异步处理器
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/3 15:25 星期一
 */
public interface AsyncHandlerListener extends EventListener {
    /*执行事件方法*/
    public void eventHandler(AsyncEvent asyncEvent);
}
