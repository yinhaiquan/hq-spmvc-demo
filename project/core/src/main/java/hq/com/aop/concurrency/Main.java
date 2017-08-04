package hq.com.aop.concurrency;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/3 15:14 星期一
 */
@Deprecated
public class Main {
    public static void main(String[] args) throws InterruptedException {
//       final Lock lock = new ReentrantLock();
//       final Condition condition = lock.newCondition();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    lock.lock();
//                    System.out.println("等待释放锁资源");
//                    condition.await();
//                    System.out.println("终于等到我了...");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } finally {
//                    lock.unlock();
//                }
//            }
//        }).start();
//        Thread.sleep(3000);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    lock.lock();
//                    System.out.println("唤醒线程");
//                    condition.signal();
//                } finally {
//                    lock.unlock();
//                }
//            }
//        }).start();

//        ConcurrentLinkedQueue<String> concurrentLinkedQueue = new ConcurrentLinkedQueue<>();
//        System.out.println(concurrentLinkedQueue.offer("123"));

        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<String>(2);
        arrayBlockingQueue.offer("123");
        arrayBlockingQueue.offer("345");
        arrayBlockingQueue.put("123123");
    }

}
