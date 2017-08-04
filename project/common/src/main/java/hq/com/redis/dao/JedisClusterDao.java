package hq.com.redis.dao;

import hq.com.aop.aopenum.RedisDB;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @title : redies集群操作API
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/4 10:43 星期二
 */
public interface JedisClusterDao {
    /**
     * String 类型set操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param value
     * @param expire  key值生命周期 若为-1则，永久保存
     */
    public void setString(String prefix, String key, String value, Long expire);

    /**
     * String 类型get操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    public String getString(String prefix, String key);

    /**
     * String 类型del操作
     *
     * @param prefix  前缀 eg:  test:
     * @param keys
     */
    public void delString(String prefix, String... keys);

    /**
     * Object 类型set操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param obj
     * @param expire  key值生命周期 若为-1则，永久保存
     */
    public void setObject(String prefix, String key, Object obj, Long expire, Class cls);

    /**
     * Object 类型get操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    public Object getObject(String prefix, String key, Class cls);

    /**
     * byte[] 类型del操作
     *
     * @param prefix  前缀 eg:  test:
     * @param keys    key的字节数组
     */
    public void delBytes(String prefix, byte[]... keys);

    /**
     * Hash 类型set操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param map
     * @param expire
     */
    public void setHash(String prefix, String key, Map<String, String> map, Long expire);

    /**
     * Hash 类型get操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    public Map<String, String> getHashAll(String prefix, String key);

    /**
     * Hash 类型set操作field
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param field
     */
    public void updateHashField(String prefix, String key, String field, String value);

    /**
     * Hash 类型get操作field
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param field
     * @return
     */
    public String getHashFieldValue(String prefix, String key, String field);

    /**
     * Hash 类型del操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param fields
     */
    public void delHash(String prefix, String key, String... fields);


    /**
     * list类型push操作
     *
     * @param prefix
     * @param key
     * @param list
     * @param expire
     */
    public void lpushList(String prefix, String key, List<String> list, Long expire);

    /**
     * list类型lrange操作
     *
     * @param prefix
     * @param key
     * @param start
     * @param stop
     * @return
     */
    public List<String> lrangeList(String prefix, String key, int start, int stop);

    /**
     * list类型llen操作
     *
     * @param prefix
     * @param key
     * @return
     */
    public Long llenList(String prefix, String key);

    /**
     * set类型add操作
     *
     * @param prefix
     * @param key
     * @param values
     * @param expire
     */
    public void sadd(String prefix, String key, Long expire, String... values);

    /**
     * set类型获取集合所有成员操作
     *
     * @param prefix
     * @param key
     * @return
     */
    public Set<String> smembers(String prefix, String key);

    /**
     * set类型求交集
     *
     * @param prefix
     * @param keys
     * @return
     */
    public Set<String> sinter(String prefix, String... keys);

    /**
     * set类型求并集
     *
     * @param prefix
     * @param keys
     * @return
     */
    public Set<String> sunion(String prefix, String... keys);

    /**
     * incr操作
     *
     * 序列服务器
     *
     * @param prefix
     * @param key
     * @return
     */
    public long incr(String prefix, String key);
}
