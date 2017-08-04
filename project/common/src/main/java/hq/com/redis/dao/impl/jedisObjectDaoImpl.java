package hq.com.redis.dao.impl;

import hq.com.aop.aopenum.RedisDB;
import hq.com.aop.utils.SerializeUtils;
import hq.com.redis.client.RedisClientTemplate;
import hq.com.redis.dao.JedisObjectDao;
import hq.com.redis.base.RedisBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.io.IOException;

/**
 * @title : redis对象操作API
 * @describle :
 * <p>
 *     <b>note:</b>
 *     1. 主要针对特殊操作，set<T> map<T,K> List<T>。
 *     2. 若需要对Object操作，建议使用jedisDao/jedisClusterDao中setObject方法，效率更高于此API。
 *     3. 对象必须序列化
 *     4. 适用redis单节点以及集群等
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/5 13:53 星期三
 */
public class jedisObjectDaoImpl extends RedisBaseDao implements JedisObjectDao {
    private static Logger log = LoggerFactory.getLogger(jedisObjectDaoImpl.class);
    private RedisClientTemplate redisClientTemplate;

    public void setRedisClientTemplate(RedisClientTemplate redisClientTemplate) {
        this.redisClientTemplate = redisClientTemplate;
    }

    /**
     * Object类型set操作
     * <p>
     * <p>
     * 若是集群客户端dbindex参数无效，可不填
     * </p>
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param obj
     * @param expire
     */
    @Override
    public void setObject(RedisDB dbindex, String prefix, String key, Object obj, Long expire) {
        JedisCommands jedisCommands = redisClientTemplate.getResource();
        try {
            String key_ = getKey(prefix,key);
            if (jedisCommands instanceof Jedis){
                Jedis jedis = (Jedis) jedisCommands;
                selectDB(jedis,dbindex);
                jedis.set(key_.getBytes(), SerializeUtils.serialize(obj));
                jedisExpire(jedis,key_,expire);
            }else if (jedisCommands instanceof JedisCluster){
                JedisCluster jedisCluster = (JedisCluster) jedisCommands;
                jedisCluster.set(key_.getBytes(),SerializeUtils.serialize(obj));
                jedisExpire(jedisCluster,key_,expire);
            }else {
                log.info("找不到redis客户端");
            }
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                redisClientTemplate.close(jedisCommands);
            } catch (IOException e) {
                log.info("redis 回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * Object类型get操作
     * <p>
     * <p>
     * 若是集群客户端dbindex参数无效，可不填
     * </p>
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    @Override
    public Object getObject(RedisDB dbindex, String prefix, String key) {
        JedisCommands jedisCommands = redisClientTemplate.getResource();
        try {
            String key_ = getKey(prefix,key);
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                selectDB(jedis,dbindex);
                return SerializeUtils.deserialize(jedis.get(key_.getBytes()));
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jedisCluster = (JedisCluster) jedisCommands;
                return SerializeUtils.deserialize(jedisCluster.get(key_.getBytes()));
            } else {
                log.info("找不到redis客户端");
                return null;
            }
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                redisClientTemplate.close(jedisCommands);
            } catch (IOException e) {
                log.info("redis 回收连接异常:{}", e.getMessage());
            }
        }
    }
}
