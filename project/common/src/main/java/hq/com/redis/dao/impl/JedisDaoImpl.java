package hq.com.redis.dao.impl;

import hq.com.aop.aopenum.RedisDB;
import hq.com.aop.utils.JprotobufUtils;
import hq.com.aop.utils.StringUtils;
import hq.com.redis.client.jedis.JedisTemplate;
import hq.com.redis.dao.JedisDao;
import hq.com.redis.base.RedisBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @title : redis API 实现类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/4 10:44 星期二
 */
public class JedisDaoImpl extends RedisBaseDao implements JedisDao {
    private static Logger log = LoggerFactory.getLogger(JedisDaoImpl.class);
    private JedisTemplate jedisTemplate;

    public void setJedisTemplate(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    /**
     * String 类型set操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀（不需要前缀则填null） eg:  test:
     * @param key
     * @param value
     * @param expire  key值生命周期 若为-1则，永久保存
     */
    public void setString(RedisDB dbindex, String prefix, String key, String value, Long expire) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            jedis.set(key_, value);
            jedisExpire(jedis, key_, expire);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * String 类型get操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀（不需要前缀则填null） eg:  test:
     * @param key
     * @return
     */
    @Override
    public String getString(RedisDB dbindex, String prefix, String key) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            return jedis.get(getKey(prefix, key));
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * String 类型批量del操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param keys
     */
    public void delString(RedisDB dbindex, String prefix, String... keys) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            jedis.del(getBatchKey(prefix, keys));
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * Object 类型set操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀（不需要前缀则填null） eg:  test:
     * @param key
     * @param obj
     * @param expire  key值生命周期 若为-1则，永久保存
     */
    public void setObject(RedisDB dbindex, String prefix, String key, Object obj, Long expire, Class cls) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            jedis.set(key_.getBytes(), JprotobufUtils.getInstance(cls).serialization(obj));
            jedisExpire(jedis, key_, expire);
        } catch (IOException e) {
            log.info("对象序列化异常:{}", e.getMessage());
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * Object 类型get操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀（不需要前缀则填null）
     * @param key
     * @return
     */
    public Object getObject(RedisDB dbindex, String prefix, String key, Class cls) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            return JprotobufUtils.getInstance(cls).deserialization(jedis.get(key_.getBytes()));
        } catch (IOException e) {
            log.info("对象序列化异常:{}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * byte[] 类型del操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param keys    key的字节数组
     */
    public void delBytes(RedisDB dbindex, String prefix, byte[]... keys) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            jedis.del(getBatechByteKey(prefix, keys));
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }

    }

    /**
     * Hash 类型set操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param map
     * @param expire
     */
    @Override
    public void setHash(RedisDB dbindex, String prefix, String key, Map<String, String> map, Long expire) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            if (StringUtils.isNotEmpty(map)) {
                Set<Map.Entry<String, String>> set = map.entrySet();
                for (Map.Entry<String, String> entry : set) {
                    jedis.hset(key_, entry.getKey(), entry.getValue());
                }
            }
            jedisExpire(jedis, key_, expire);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * Hash 类型get操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    @Override
    public Map<String, String> getHashAll(RedisDB dbindex, String prefix, String key) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            return jedis.hgetAll(key_);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * Hash 类型set操作field
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @param field
     * @param value
     */
    @Override
    public void updateHashField(RedisDB dbindex, String prefix, String key, String field, String value) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            jedis.hset(key_, field, value);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * Hash 类型get操作field
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @param field
     * @return
     */
    @Override
    public String getHashFieldValue(RedisDB dbindex, String prefix, String key, String field) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            return jedis.hget(key_, field);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * Hash 类型del操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param fields
     */
    @Override
    public void delHash(RedisDB dbindex, String prefix, String key, String... fields) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            jedis.hdel(getKey(prefix, key), fields);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * list类型push操作
     * <p>
     * 偏移量也可以是负数，表示偏移量是从list尾部开始计数。 例如， -1 表示列表的最后一个元素，-2 是倒数第二个，以此类推
     * </p>
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @param list
     * @param expire
     */
    @Override
    public void lpushList(RedisDB dbindex, String prefix, String key, List<String> list, Long expire) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            jedis.lpush(key_, (String[]) list.toArray(new String[list.size()]));
            jedisExpire(jedis, key_, expire);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * list类型lrange操作
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @param start
     * @param stop
     * @return
     */
    @Override
    public List<String> lrangeList(RedisDB dbindex, String prefix, String key, int start, int stop) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            return jedis.lrange(key_, start, stop);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * list类型llen操作
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @return
     */
    @Override
    public Long llenList(RedisDB dbindex, String prefix, String key) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            return jedis.llen(key_);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return 0l;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * 获取DB大小
     *
     * @param dbindex
     * @return
     */
    @Override
    public Long dbsize(RedisDB dbindex) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            return jedis.dbSize();
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return 0l;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * set类型add操作
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @param expire
     * @param values
     */
    @Override
    public void sadd(RedisDB dbindex, String prefix, String key, Long expire, String... values) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        String key_ = getKey(prefix, key);
        try {
            jedis.sadd(key_, values);
            jedisExpire(jedis, key_, expire);
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * set类型获取集合所有成员操作
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @return
     */
    @Override
    public Set<String> smembers(RedisDB dbindex, String prefix, String key) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            return jedis.smembers(getKey(prefix, key));
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * set类型求交集
     *
     * @param dbindex
     * @param prefix
     * @param keys
     * @return
     */
    @Override
    public Set<String> sinter(RedisDB dbindex, String prefix, String... keys) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            return jedis.sinter(getBatchKey(prefix, keys));
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * set类型求并集
     *
     * @param dbindex
     * @param prefix
     * @param keys
     * @return
     */
    @Override
    public Set<String> sunion(RedisDB dbindex, String prefix, String... keys) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            return jedis.sunion(getBatchKey(prefix, keys));
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return null;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }

    /**
     * incr操作
     *
     * <p>
     *    <b>note:</b>
     *       将 key 中储存的数字值增一。如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行 INCR 操作。
     *       通常用作序列服务器获取序列
     * </p>
     * @param dbindex
     * @param prefix
     * @param key
     * @return
     */
    @Override
    public long incr(RedisDB dbindex, String prefix, String key) {
        Jedis jedis = jedisTemplate.getResource();
        selectDB(jedis, dbindex);
        try {
            return jedis.incr(getKey(prefix,key));
        } catch (Exception e) {
            log.info("redis异常:{}", e.getMessage());
            return 0;
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("redis回收连接异常:{}", e.getMessage());
            }
        }
    }
}
