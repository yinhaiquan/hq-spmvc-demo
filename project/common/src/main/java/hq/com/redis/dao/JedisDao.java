package hq.com.redis.dao;

import hq.com.aop.aopenum.RedisDB;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @title : redis API
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/4 10:41 星期二
 */
public interface JedisDao {
    /**
     * String 类型set操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param value
     * @param expire  key值生命周期 若为-1则，永久保存
     */
    public void setString(RedisDB dbindex, String prefix, String key, String value, Long expire);

    /**
     * String 类型get操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    public String getString(RedisDB dbindex, String prefix, String key);

    /**
     * String 类型del操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param keys
     */
    public void delString(RedisDB dbindex, String prefix, String... keys);

    /**
     * Object 类型set操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param obj
     * @param expire  key值生命周期 若为-1则，永久保存
     */
    public void setObject(RedisDB dbindex, String prefix, String key, Object obj, Long expire, Class cls);

    /**
     * Object 类型get操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    public Object getObject(RedisDB dbindex, String prefix, String key, Class cls);

    /**
     * byte[] 类型del操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param keys    key的字节数组
     */
    public void delBytes(RedisDB dbindex, String prefix, byte[]... keys);

    /**
     * Hash 类型set操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param map
     * @param expire
     */
    public void setHash(RedisDB dbindex, String prefix, String key, Map<String, String> map, Long expire);

    /**
     * Hash 类型get操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    public Map<String, String> getHashAll(RedisDB dbindex, String prefix, String key);

    /**
     * Hash 类型set操作field
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param field
     */
    public void updateHashField(RedisDB dbindex, String prefix, String key, String field, String value);

    /**
     * Hash 类型get操作field
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param field
     * @return
     */
    public String getHashFieldValue(RedisDB dbindex, String prefix, String key, String field);

    /**
     * Hash 类型del操作
     *
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param fields
     */
    public void delHash(RedisDB dbindex, String prefix, String key, String... fields);


    /**
     * list类型push操作
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @param list
     * @param expire
     */
    public void lpushList(RedisDB dbindex, String prefix, String key, List<String> list, Long expire);

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
    public List<String> lrangeList(RedisDB dbindex, String prefix, String key, int start, int stop);

    /**
     * list类型llen操作
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @return
     */
    public Long llenList(RedisDB dbindex, String prefix, String key);

    /**
     * 获取DB大小
     *
     * @param dbindex
     * @return
     */
    public Long dbsize(RedisDB dbindex);

    /**
     * set类型add操作
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @param values
     * @param expire
     */
    public void sadd(RedisDB dbindex, String prefix, String key, Long expire, String... values);

    /**
     * set类型获取集合所有成员操作
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @return
     */
    public Set<String> smembers(RedisDB dbindex, String prefix, String key);

    /**
     * set类型求交集
     *
     * @param dbindex
     * @param prefix
     * @param keys
     * @return
     */
    public Set<String> sinter(RedisDB dbindex, String prefix, String... keys);

    /**
     * set类型求并集
     *
     * @param dbindex
     * @param prefix
     * @param keys
     * @return
     */
    public Set<String> sunion(RedisDB dbindex, String prefix, String... keys);

    /**
     * incr操作
     *
     * 序列服务器
     *
     * @param dbindex
     * @param prefix
     * @param key
     * @return
     */
    public long incr(RedisDB dbindex, String prefix, String key);
}
