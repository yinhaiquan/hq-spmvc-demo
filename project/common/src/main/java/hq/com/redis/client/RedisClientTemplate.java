package hq.com.redis.client;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.io.IOException;

/**
 * @Describle: redis客户端抽象模板
 * <p>
 * <b>note:</b>
 * 该模板适用于单节点redis、redis分布式集群,或者基于sentinel redis HA集群；
 * 通过getResource()方法获取jedis实例对redis进行操作
 * </p>
 * @Author: YinHq
 * @Date: Created By 下午 6:01 2017/6/4 0004
 * @Modified By
 */
public abstract class RedisClientTemplate {
    public abstract JedisCommands getResource();

    public void close(JedisCommands jc) throws IOException {
        if (null != jc) {
            if (jc instanceof Jedis) {
                ((Jedis) jc).close();
            } else if (jc instanceof JedisCluster) {
                //redis 3.0 集群会自动关闭，无需手动close
            }
        }
    }
}
