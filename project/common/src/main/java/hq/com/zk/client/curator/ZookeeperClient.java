package hq.com.zk.client.curator;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @title : zk客户端
 * @describle :
 * <p>
 * <b>note:</b>
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * 1. Curator内部实现的几种重试策略:
 *      ExponentialBackoffRetry:重试指定的次数, 且每一次重试之间停顿的时间逐渐增加.
 *      RetryNTimes:指定最大重试次数的重试策略
 *      RetryOneTime:仅重试一次
 *      RetryUntilElapsed:一直重试直到达到规定的时间
 *      <p>
 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
 * 2. Versions:(Curator版本跟zk版本差异)
 *      The are currently two released versions of Curator, 2.x.x and 3.x.x:
 *      <p>
 *      Curator 2.x.x - compatible with both ZooKeeper 3.4.x and ZooKeeper 3.5.x
 *      Curator 3.x.x - compatible only with ZooKeeper 3.5.x and includes support for new features such as dynamic reconfiguration, etc.
 *      <p>
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * 3. 每个节点被称为znode, 为znode节点依据其特性, 又可以分为如下类型:
 * 　   PERSISTENT: 永久节点
 * 　   EPHEMERAL: 临时节点, 会随session(client disconnect)的消失而消失
 * 　   PERSISTENT_SEQUENTIAL: 永久节点, 其节点的名字编号是单调递增的(即节点后面会加上递增数值)
 *      EPHEMERAL_SEQUENTIAL: 临时节点, 其节点的名字编号是单调递增的(即节点后面会加上递增数值)
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/6 17:45 星期四
 */
@Deprecated
public class ZookeeperClient {
    /*zk访问地址*/
    private String ips;
    /*连接超时时间 默认6s*/
    private int connectionTimeoutMs = 6000;
    /*session超时时间 默认6s*/
    private int sessionTimeoutMs = 6000;
    private boolean canBeReadOnly = false;
    /*第一次重试等待时间 默认6s*/
    private int baseSleepTimesMs = 6000;
    /*重试指定的次数, 且每一次重试之间停顿的时间逐渐增加 默认3次*/
    private int maxRetries = 3;
    /*命名空间*/
    public String namespace = "hq";

    public ZookeeperClient(String ips) {
        this.ips = ips;
    }

    public ZookeeperClient(String ips, int connectionTimeoutMs) {
        this(ips);
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public ZookeeperClient(String ips, int connectionTimeoutMs, int sessionTimeoutMs) {
        this(ips, connectionTimeoutMs);
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public ZookeeperClient(String ips, int connectionTimeoutMs, int sessionTimeoutMs, int baseSleepTimesMs) {
        this(ips, connectionTimeoutMs, sessionTimeoutMs);
        this.baseSleepTimesMs = baseSleepTimesMs;
    }

    public ZookeeperClient(String ips, int connectionTimeoutMs, int sessionTimeoutMs, int baseSleepTimesMs, int maxRetries) {
        this(ips, connectionTimeoutMs, sessionTimeoutMs, baseSleepTimesMs);
        this.maxRetries = maxRetries;
    }

    public ZookeeperClient(String ips, int connectionTimeoutMs, int sessionTimeoutMs, int baseSleepTimesMs, int maxRetries, String namespace) {
        this(ips, connectionTimeoutMs, sessionTimeoutMs, baseSleepTimesMs, maxRetries);
        this.namespace = namespace;
    }

    /**
     * 获取zk客户端连接
     *
     * @return
     */
    @Deprecated
    public CuratorFramework getClient() {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(ips)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .canBeReadOnly(canBeReadOnly)
                .retryPolicy(new ExponentialBackoffRetry(baseSleepTimesMs, maxRetries))
                .namespace(namespace)
                .defaultData(null)
                .build();
        client.start();
        return client;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public long getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public long getBaseSleepTimesMs() {
        return baseSleepTimesMs;
    }

    public void setBaseSleepTimesMs(int baseSleepTimesMs) {
        this.baseSleepTimesMs = baseSleepTimesMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }
}
