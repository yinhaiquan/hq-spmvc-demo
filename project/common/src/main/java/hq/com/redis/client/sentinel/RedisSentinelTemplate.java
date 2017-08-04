package hq.com.redis.client.sentinel;

import hq.com.redis.client.RedisClientTemplate;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/27 15:53 星期六
 */
public class RedisSentinelTemplate extends RedisClientTemplate {
    private static Logger log = LoggerFactory.getLogger(RedisSentinelTemplate.class);
    private final static String REG_D = ",";
    private final static String REG_M = ":";
    private String ips;
    private String password;
    private JedisSentinelPool pool;
    private GenericObjectPoolConfig genericObjectPoolConfig;

    public RedisSentinelTemplate() {
    }

    public RedisSentinelTemplate(String ips, String password) {
        this.ips = ips;
        this.password = password;
    }

    private Set<String> getClusterInfo() {
        Set<String> set = new HashSet<String>();
        if (null != ips && ips.length() > 0) {
            String ipPort[] = ips.split(REG_D);
            for (String ip : ipPort) {
                String[] server = ip.split(REG_M);
                set.add(new HostAndPort(server[0], Integer.parseInt(server[1])).toString());
            }
        }
        return set;
    }

    @Override
    public Jedis getResource() {
        if (null == pool) {
            Set<String> set = getClusterInfo();
            pool = new JedisSentinelPool("pool", set, genericObjectPoolConfig);
        }
        Jedis jedis = pool.getResource();
        if (null != password && !"".equals(password)) {
            jedis.auth(password);
        }
        return jedis;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }
}
