package hq.com.redis.lock;

import hq.com.aop.exception.RedisLockException;
import hq.com.aop.utils.BigDecimalUtils;
import hq.com.aop.utils.StringUtils;
import hq.com.redis.client.RedisClientTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCommands;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;

/**
 * @title : redis分布式锁
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/5 15:26 星期三
 */
public class RedisDistributedLock{
    private static Logger log = LoggerFactory.getLogger(RedisDistributedLock.class);
    /* redis客户端 */
    private RedisClientTemplate redisClientTemplate;
    /* redis实例 */
    private JedisCommands jedisCommands;
    /* 锁 */
    private String lock;
    /* 锁超时时间(单位/ms) 默认1分钟 */
    private long lockTimeOut = 60*1000;
    /* 锁等待重试次数 默认6次 */
    private int lockWaitReTryTimes = 6;
    /* 锁状态 */
    private boolean locked;

    public RedisDistributedLock(RedisClientTemplate redisClientTemplate, String lock) {
        this.redisClientTemplate = redisClientTemplate;
        this.lock = lock;
        jedisCommands = redisClientTemplate.getResource();
    }

    public RedisDistributedLock(RedisClientTemplate redisClientTemplate, String lock, long lockTimeOut) {
        this(redisClientTemplate,lock);
        this.lockTimeOut = lockTimeOut;
    }

    public RedisDistributedLock(RedisClientTemplate redisClientTemplate, String lock, long lockTimeOut, int lockWaitReTryTimes) {
        this(redisClientTemplate, lock, lockTimeOut);
        this.lockWaitReTryTimes = lockWaitReTryTimes;
    }

    /**
     * 获取锁资源
     * @return
     * @throws InterruptedException
     */
    public synchronized boolean lock() throws RedisLockException {
        try {
            int reTryTimes = 1;
            while(reTryTimes<=lockWaitReTryTimes){
                long expires = System.currentTimeMillis()+lockTimeOut+1;
                //设置锁超时时间
                String expireLockTime = String.valueOf(expires);
                long ret = this.setNX(lock,expireLockTime);
                if (ret==1){
                    //获取锁
                    locked = true;
                    return true;
                }

                /**
                 * 锁超时
                 *
                 * 判断锁超时时间，已防止死锁
                 */
                String currentLockTime = this.get(lock);
                long currentLockTimeVal = Long.parseLong(currentLockTime);
                long systemCurrentTimeVal = System.currentTimeMillis();
                System.out.println("当前锁超时时间-系统当前时间:ms"+(currentLockTimeVal-systemCurrentTimeVal));
                //判断当前锁值是否为空，否则判断锁是否超时(currentLockTimeVal<System.currentTimeMillis())
                if (StringUtils.isNotEmpty(currentLockTime)&&currentLockTimeVal<systemCurrentTimeVal){
                    //获取上一个锁超时时间，并设置现在锁的超时时间
                    String oldLockTime = this.getSet(lock,expireLockTime);
                    /**
                     * 由于jedis.getSet是同步的，所以只有一个线程才能获取到上一个锁的超时时间
                     * 当多个线程同时执行到此处，由于只有一个线程的设置值和当前值相同，他才有权利获取锁
                     */
                    if (StringUtils.isNotEmpty(oldLockTime)&&oldLockTime.equals(currentLockTime)){
                        //获取锁
                        locked = true;
                        return true;
                    }
                }
                System.out.println("获取锁重试第"+reTryTimes+"次");
                reTryTimes++;


                /**
                 * 使用随机数保证每个线程等待时间公平
                 */
                BigDecimal time = BigDecimalUtils.op(0, BigDecimalUtils.BigDecimalType.MULTIPLY,String.valueOf(Math.random()),"10000");
                System.out.println("重试耗时:ms"+time.longValue());
                Thread.sleep(time.longValue());
            }
        } catch (InterruptedException e) {
            log.info("redis获取锁异常:{}",e.getMessage());
            throw new RedisLockException("redis获取锁异常",e);
        }
        locked = false;
        return false;
    }

    /**
     * 释放锁资源
     */
    public synchronized void unlock(){
        //只有拥有锁资源线程方可释放锁资源
        if (locked){
            jedisCommands.del(lock);
            locked = false;
        }else{
            log.info("在竞争锁等待重试次数内仍未获取到锁，sorry，我不要锁了，干自己事去了。。。。");
            System.out.println("在竞争锁等待重试次数内仍未获取到锁，sorry，我不要锁了，干自己事去了。。。。");
        }
    }

    public String get(String key){
        return jedisCommands.get(key);
    }

    public long setNX(String key,String value){
        return jedisCommands.setnx(key,value);
    }

    public String getSet(String key,String value){
        return jedisCommands.getSet(key,value);
    }

    public RedisClientTemplate getRedisClientTemplate() {
        return redisClientTemplate;
    }

    public void setRedisClientTemplate(RedisClientTemplate redisClientTemplate) {
        this.redisClientTemplate = redisClientTemplate;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public long getLockTimeOut() {
        return lockTimeOut;
    }

    public void setLockTimeOut(long lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
    }

    public int getLockWaitReTryTimes() {
        return lockWaitReTryTimes;
    }

    public void setLockWaitReTryTimes(int lockWaitReTryTimes) {
        this.lockWaitReTryTimes = lockWaitReTryTimes;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
