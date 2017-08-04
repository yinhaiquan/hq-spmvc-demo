package hq.com.aop.concurrency;

/**
 * @title : 事件源接口
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/3 15:32 星期一
 */
public interface AsyncEventSource {
    /*注册监听器*/
    public abstract boolean addListener(AsyncObject asyncObject);

    /*唤醒线程执行事件方法*/
    public abstract void signalAll();
}
