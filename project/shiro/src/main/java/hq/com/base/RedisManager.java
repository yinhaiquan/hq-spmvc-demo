package hq.com.base;

import hq.com.aop.utils.StringUtils;
import hq.com.enums.CacheTypeEnums;
import hq.com.redis.client.RedisClientTemplate;
import hq.com.redis.client.jedis.JedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Describle: redis客户端
 * @Author: YinHq
 * @Date: Created By 下午 11:04 2017/6/3 0003
 * @Modified By
 */
public class RedisManager {
    private static Logger log = LoggerFactory.getLogger(RedisManager.class);
    /*用于redis分布式集群存储缓存至hash 用于获取keys,另再存储一份至CLUSTER_HASH_PRE以设置有效时间，由于hash不能针对feild设置有效时间*/
    private static final String CLUSTER_HASH_PRE = "cluster_hash_pre:";
    private RedisClientTemplate jedisTemplate;
    private int expire = 0;

    public RedisManager(RedisClientTemplate jedisTemplate, int expire) {
        this.jedisTemplate = jedisTemplate;
        this.expire = expire;
        StringBuffer sb = new StringBuffer("*               ");
        sb.append(StringUtils.isNotEmpty(jedisTemplate) ? "redis集群启动成功!" : "redis集群加载失败!");
        sb.append("              *");
        log.info("***********************************************");
        log.info(sb.toString());
        log.info("***********************************************");
    }

    private void close(JedisCommands jedisCommands) {
        try {
            jedisTemplate.close(jedisCommands);
        } catch (IOException e) {
            log.info("redis释放链接异常:{}", e.getMessage());
        }
    }

    /*字符串get操作*/
    public String get(String key) {
        JedisCommands jedisCommands = jedisTemplate.getResource();
        String value = null;
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                value = jedis.get(key);
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jc = (JedisCluster) jedisCommands;
                value = jc.get(key);
            }
        } finally {
            close(jedisCommands);
        }
        return value;
    }

    /*字符串set操作*/
    public void set(String key, String value, int expire) {
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                jedis.set(key, value);
                jedis.expire(key, expire);
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jc = (JedisCluster) jedisCommands;
                jc.set(key, value);
                jc.expire(key, expire);
            }
        } finally {
            close(jedisCommands);
        }
    }

    /*字符串del操作*/
    public void remve(String key) {
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                jedis.del(key);
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jc = (JedisCluster) jedisCommands;
                jc.del(key);
            }
        } finally {
            close(jedisCommands);
        }

    }

    /**
     * 字节get操作
     * <p>
     * <b>note:</b>
     * redis集群中需要根据缓存类型执行不同操作
     * 缓存类型：session缓存和权限缓存
     * </p>
     *
     * @param key
     * @return
     */
    public byte[] get(byte[] key, CacheTypeEnums cte) {
        byte[] value = null;
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                value = jedis.get(key);
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jc = (JedisCluster) jedisCommands;
                byte v[] = null;
                switch (cte) {
                    case SESSIONCACHE:
                        v = jc.hget(CLUSTER_HASH_PRE.getBytes(), key);
                        break;
                    case AUTHORIZATIONCACHE:
                        v = jc.get(key);
                        break;
                }
                value = StringUtils.isNotEmpty(v) ? v : null;
            }
        } finally {
            close(jedisCommands);
        }
        return value;
    }

    /*权限缓存set操作*/
    public byte[] set(byte[] key, byte[] value) {
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                jedis.set(key, value);
                if (this.expire != 0) {
                    jedis.expire(key, this.expire);
                }
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jc = (JedisCluster) jedisCommands;
                jc.set(key, value);
                if (this.expire != 0) {
                    jc.expire(key, this.expire);
                }
            }
        } finally {
            close(jedisCommands);
        }
        return value;
    }

    /*session缓存set操作*/
    public byte[] set(byte[] key, byte[] value, int expire) {
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                jedis.set(key, value);
                if (expire != 0) {
                    jedis.expire(key, expire);
                }
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jc = (JedisCluster) jedisCommands;
                jc.hset(CLUSTER_HASH_PRE.getBytes(), key, value);
                jc.set(key, value);
                if (expire != 0) {
                    jc.expire(key, expire);
                }
            }
        } finally {
            close(jedisCommands);
        }
        return value;
    }

    /**
     * 字节DEL操作
     * <p>
     * <b>note:</b>
     * redis集群中需要根据缓存类型执行不同操作
     * 缓存类型：session缓存和权限缓存
     * </p>
     *
     * @param key
     * @return
     */
    public void del(byte[] key, CacheTypeEnums cte) {
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                jedis.del(key);
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jc = (JedisCluster) jedisCommands;
                switch (cte) {
                    case SESSIONCACHE:
                        jc.hdel(CLUSTER_HASH_PRE.getBytes(), key);
                        jc.del(key);
                        break;
                    case AUTHORIZATIONCACHE:
                        jc.del(key);
                        break;
                }

            }
        } finally {
            close(jedisCommands);
        }
    }

    public void flushDB() {
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                jedis.flushDB();
            } else if (jedisCommands instanceof JedisCluster) {
                //redis集群无此方法
            }
        } finally {
            close(jedisCommands);
        }
    }

    public Long dbSize() {
        Long dbSize = 0L;
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                dbSize = jedis.dbSize();
            } else if (jedisCommands instanceof JedisCluster) {
                //redis集群无此方法
            }
        } finally {
            close(jedisCommands);
        }
        return dbSize;
    }


    /**
     *   redis集群遍历每个连接池做法，可通过单个redis节点操作
     *   Set<byte[]> keysMatched = new HashSet<>();
     *   logger.info(jedisCluster.getClusterNodes().size());
     *   Iterator<JedisPool> poolIterator = jedisCluster.getClusterNodes().values().iterator();
     *   while (poolIterator.hasNext()) {
     *   JedisPool pool = poolIterator.next();
     *   Jedis jedis = pool.getResource();
     *   try {
     *   keysMatched.addAll(jedis.keys(pattern.getBytes()));
     *   } catch (Exception ex) {
     *   logger.error("Exception in cache service: {} ", ex);
     *   } finally {
     *   jedis.close();
     *   }
     *   }
     *     return keysMatched;
     * @param pattern
     * @return
     */
    public Set<byte[]> keys(String pattern) {
        Set<byte[]> keys = null;
        JedisCommands jedisCommands = jedisTemplate.getResource();
        try {
            if (jedisCommands instanceof Jedis) {
                Jedis jedis = (Jedis) jedisCommands;
                keys = jedis.keys(pattern.getBytes());
            } else if (jedisCommands instanceof JedisCluster) {
                JedisCluster jc = (JedisCluster) jedisCommands;
                Set<byte[]> keys_1 = jc.hkeys(CLUSTER_HASH_PRE.getBytes());
                Set<byte[]> keys_2 = new HashSet<>();
                if (StringUtils.isNotEmpty(keys_1)) {
                    Iterator<byte[]> iterator = keys_1.iterator();
                    while (iterator.hasNext()) {
                        byte[] key = iterator.next();
                        byte[] k_1 = jc.get(key);
                        if (StringUtils.isNotEmpty(k_1)) {
                            keys_2.add(key);
                        }
                    }
                    keys = keys_2;
                }
            }
        } finally {
            close(jedisCommands);
        }
        return keys;
    }

    public RedisClientTemplate getJedisTemplate() {
        return jedisTemplate;
    }

    public void setJedisTemplate(RedisClientTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
