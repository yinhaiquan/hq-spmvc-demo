package hq.com.redis.client.cluster;

import hq.com.redis.client.RedisClientTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @title : redis cluster api
 * @describle :
 * <p>
 * redis 集群接入操作
 * </p>
 * Create By yinhaiquan
 * @date 2017/5/27 15:53 星期六
 */
public class RedisClusterTemplate extends RedisClientTemplate {
    private static Logger log = LoggerFactory.getLogger(RedisClusterTemplate.class);

    private JedisCluster jedisCluster;

    public RedisClusterTemplate() {
    }

    public JedisCluster getResource() {
        return jedisCluster;
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }
}
