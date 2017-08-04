package hq.com.aop.concurrency.impl;

import hq.com.aop.concurrency.AsyncEvent;
import hq.com.aop.concurrency.AsyncEventSource;
import hq.com.aop.concurrency.AsyncHandlerListener;
import hq.com.aop.concurrency.AsyncObject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @title : 事件源实现类
 * @describle :
 * <p>
 *     <b>note:</b>
 *     demo在api子项目test中
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/3 15:39 星期一
 */
public class AsyncEventSourceImpl implements AsyncEventSource {
    /*并发队列*/
    private static final ConcurrentLinkedQueue<AsyncObject> queue = new ConcurrentLinkedQueue<>();
    /*线程池*/
    private ThreadPoolTaskExecutor taskExecutor;
    /*锁*/
    private final Lock lock = new ReentrantLock();
    /*唤醒/等待条件*/
    private final Condition awaken = lock.newCondition();

    @Override
    public boolean addListener(AsyncObject amiObject) {
        return queue.offer(amiObject);
    }

    @Override
    public void signalAll() {
        if (!queue.isEmpty()) {
            taskExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    lock.lock();
                    try {
                        awaken.signal();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            });
        }
    }

    /*初始化对象前操作，挂起线程，提高并发量*/
    @PostConstruct
    private void init() {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    lock.lock();
                    try {
                        /*线程进入等待状态，并释放锁资源*/
                        awaken.await();
                        /*等待事件执行*/
                        while (!queue.isEmpty()) {
                            start(queue.poll());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        });
    }

    /*执行事件*/
    private void start(AsyncObject asyncObject) {
        try {
            AsyncHandlerListener handlerListener = asyncObject.getAsyncHandlerListener();
            AsyncEvent asyncEvent = new AsyncEvent(handlerListener);
            asyncEvent.setParams(asyncObject.getParams());
            handlerListener.eventHandler(asyncEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ThreadPoolTaskExecutor getTaskExecutor() {
        return taskExecutor;
    }

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }
}
