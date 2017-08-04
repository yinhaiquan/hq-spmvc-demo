package hq.com.zk.lock;

import hq.com.aop.exception.ZookeeperLockException;
import hq.com.aop.utils.StringUtils;
import hq.com.zk.base.ZookeeperBaseDao;
import hq.com.zk.client.curator.ZookeeperClient;
import hq.com.zk.dao.ZookeeperDao;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @title : 基于zookeeper分布式锁
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/6 18:49 星期四
 */
public class ZookeeperDistributedLock extends ZookeeperBaseDao{
    private static Logger log = LoggerFactory.getLogger(ZookeeperDistributedLock.class);
    /**锁节点名称*/
    private String lockName;
    /**锁*/
    private InterProcessMutex lock;
    /**zk客户端连接*/
    private CuratorFramework client;
    /**竞争锁等待时间(单位/s) 默认值-1 即未获取到锁资源一直等待(等待时间为-1时，unit必须为null)*/
    private long waitTime;


    public ZookeeperDistributedLock(CuratorFramework client,String lockName) {
        this.lockName = lockName;
        lock = new InterProcessMutex(client,format(lockName));
    }

    public ZookeeperDistributedLock(String lockName, CuratorFramework client, long waitTime) {
        this(client,lockName);
        this.waitTime = waitTime;
    }

    /**
     * 获取锁资源
     *
     * @throws ZookeeperLockException
     */
    public void lock() throws ZookeeperLockException {
        try {
            /**多个线程同时并发执行时，只有一个线程获取锁资源，其他线程等待获取锁*/
            TimeUnit tu = null;
            if (waitTime!=-1){
                tu = TimeUnit.SECONDS;
            }
            lock.acquire(waitTime, tu);
        } catch (Exception e) {
            log.info("zookeeper获取锁异常:{}",e.getMessage());
            throw new ZookeeperLockException("zookeeper获取锁异常",e);
        }
    }

    /**
     * 释放锁资源
     */
    public void unlock() throws ZookeeperLockException {
        if (StringUtils.isNotEmpty(lock)){
            try {
                //只有拥有锁资源线程方可释放锁资源
                if (lock.isAcquiredInThisProcess()){
                    lock.release();
                    System.out.println("释放锁资源");
                }else{
                    log.info("在竞争锁等待时间内仍未获取到锁，sorry，我不要锁了，干自己事去了。。。。");
                    System.out.println("在竞争锁等待时间内仍未获取到锁，sorry，我不要锁了，干自己事去了。。。。");
                }
            } catch (Exception e) {
                log.info("zookeeper释放锁异常:{}",e.getMessage());
                throw new ZookeeperLockException("zookeeper释放锁异常",e);
            }
        }
    }

    public String getLockName() {
        return lockName;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName;
    }

    public CuratorFramework getClient() {
        return client;
    }

    public void setClient(CuratorFramework client) {
        this.client = client;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(long waitTime) {
        this.waitTime = waitTime;
    }

    public InterProcessMutex getLock() {
        return lock;
    }

    public void setLock(InterProcessMutex lock) {
        this.lock = lock;
    }
}
