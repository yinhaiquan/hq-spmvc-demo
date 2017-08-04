package hq.com.redis.client.jedis;

import hq.com.redis.client.RedisClientTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

/**
 * @title : jedis api
 * @describle :
 * <p>
 * 单节点接入操作
 * </p>
 * Create By yinhaiquan
 * @date 2017/5/27 10:56 星期六
 */
public class JedisTemplate extends RedisClientTemplate {
    public static Logger log = LoggerFactory.getLogger(JedisTemplate.class);

    private int times = 3; //重连次数
    private int timeout = 20; //连接超时时间
    private String host;//redis服务ip地址
    private String password;//redis服务密码
    private int port;//redis服务端口
    private int max_active = 8;//可用连接实例的最大数目，默认值为8
    private int max_idle = 8;//控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8
    private int max_wait = -1;//等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException

    private static JedisPool jedisPool;//redis连接池
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    private Integer count = new Integer(0);

    private JedisTemplate() {
    }

    public JedisTemplate(String host, String password, int port) {
        this.host = host;
        this.password = password;
        this.port = port;
    }

    {
        threadLocal.set(count);
    }


    private Jedis getJedis() throws Exception {
        Jedis jedis = null;
        if (null == jedisPool) {
            log.info("初始化redis连接池配置!");
            JedisPoolConfig jpc = new JedisPoolConfig();
            jpc.setMaxIdle(max_idle);
            jpc.setMaxWaitMillis(max_wait);
            jedisPool = new JedisPool(jpc, host, port, timeout * 20);
        }
        jedis = jedisPool.getResource();
        if (null != password && !"".equals(password)) {
            jedis.auth(password);
        }
        return jedis;
    }

    /**
     * 获取jedis实例
     *
     * @return
     */
    @Override
    public Jedis getResource() {
        try {
            return getJedis();
        } catch (Exception e) {
            count = threadLocal.get();
            if (count < times) {
                ++count;
                threadLocal.set(count);
                System.err.println("网络波动redis服务第[" + count + "]次重连中，请稍后。。。。。。");
                log.info("网络波动redis服务[{}:{}]第[{}]次重连中，请稍后。。。。。。", host, port, count);
                try {
                    Thread.sleep(timeout * 1000);
                } catch (InterruptedException e1) {
                }
                return getResource();
            } else {
                log.info("redis服务[{}:{}]连不上。。。", host, port);
                System.out.println("redis服务连不上。。。");
                return null;
            }
        }
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMax_active() {
        return max_active;
    }

    public void setMax_active(int max_active) {
        this.max_active = max_active;
    }

    public int getMax_idle() {
        return max_idle;
    }

    public void setMax_idle(int max_idle) {
        this.max_idle = max_idle;
    }

    public int getMax_wait() {
        return max_wait;
    }

    public void setMax_wait(int max_wait) {
        this.max_wait = max_wait;
    }

}
