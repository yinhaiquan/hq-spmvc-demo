package hq.com.redis.dao.impl;

import hq.com.aop.utils.JprotobufUtils;
import hq.com.aop.utils.StringUtils;
import hq.com.redis.dao.JedisClusterDao;
import hq.com.redis.base.RedisBaseDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisCluster;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @title : redis 集群 API 实现类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/4 10:45 星期二
 */
public class JedisClusterDaoImpl extends RedisBaseDao implements JedisClusterDao {
    private static Logger log = LoggerFactory.getLogger(JedisClusterDaoImpl.class);
    private JedisCluster jedisCluster;

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    /**
     * String 类型set操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param value
     * @param expire  key值生命周期 若为-1则，永久保存
     */
    @Override
    public void setString(String prefix, String key, String value, Long expire) {
        String key_ = getKey(prefix, key);
        try{
            jedisCluster.set(key_,value);
            jedisExpire(jedisCluster,key_,expire);
        } catch (Exception e){
            System.out.println(e.getMessage());
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * String 类型get操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    @Override
    public String getString(String prefix, String key) {
        String key_ = getKey(prefix, key);
        try{
            return jedisCluster.get(key_);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * String 类型del操作
     *
     * @param prefix  前缀 eg:  test:
     * @param keys
     */
    @Override
    public void delString(String prefix, String... keys) {
        try{
            jedisCluster.del(getBatchKey(prefix,keys));
        }catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * Object 类型set操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param obj
     * @param expire  key值生命周期 若为-1则，永久保存
     * @param cls
     */
    @Override
    public void setObject(String prefix, String key, Object obj, Long expire, Class cls) {
        String key_ = getKey(prefix, key);
        try{
            jedisCluster.set(key_.getBytes(), JprotobufUtils.getInstance(cls).serialization(obj));
            jedisExpire(jedisCluster,key_,expire);
        }catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * Object 类型get操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param cls
     * @return
     */
    @Override
    public Object getObject(String prefix, String key, Class cls) {
        String key_ = getKey(prefix, key);
        try{
            return JprotobufUtils.getInstance(cls).deserialization(jedisCluster.get(key_.getBytes()));
        }catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * byte[] 类型del操作
     *
     * @param prefix  前缀 eg:  test:
     * @param keys    key的字节数组
     */
    @Override
    public void delBytes(String prefix, byte[]... keys) {
        try{
            jedisCluster.del(getBatechByteKey(prefix,keys));
        }catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * Hash 类型set操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param map
     * @param expire
     */
    @Override
    public void setHash(String prefix, String key, Map<String, String> map, Long expire) {
        String key_ = getKey(prefix, key);
        try{
            if (StringUtils.isNotEmpty(map)) {
                Set<Map.Entry<String, String>> set = map.entrySet();
                for (Map.Entry<String, String> entry : set) {
                    jedisCluster.hset(key_, entry.getKey(), entry.getValue());
                }
            }
            jedisExpire(jedisCluster, key_, expire);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * Hash 类型get操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    @Override
    public Map<String, String> getHashAll(String prefix, String key) {
        String key_ = getKey(prefix, key);
        try{
            return jedisCluster.hgetAll(key_);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * Hash 类型set操作field
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param field
     * @param value
     */
    @Override
    public void updateHashField(String prefix, String key, String field, String value) {
        String key_ = getKey(prefix, key);
        try{
            jedisCluster.hset(key_,field,value);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * Hash 类型get操作field
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param field
     * @return
     */
    @Override
    public String getHashFieldValue(String prefix, String key, String field) {
        String key_ = getKey(prefix, key);
        try{
            return jedisCluster.hget(key_,field);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * Hash 类型del操作
     *
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param fields
     */
    @Override
    public void delHash(String prefix, String key, String... fields) {
        String key_ = getKey(prefix, key);
        try{
            jedisCluster.hdel(key_,fields);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * list类型push操作
     *
     * @param prefix
     * @param key
     * @param list
     * @param expire
     */
    @Override
    public void lpushList(String prefix, String key, List<String> list, Long expire) {
        String key_ = getKey(prefix, key);
        try{
            jedisCluster.lpush(key_, (String[]) list.toArray(new String[list.size()]));
            jedisExpire(jedisCluster, key_, expire);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * list类型lrange操作
     *
     * @param prefix
     * @param key
     * @param start
     * @param stop
     * @return
     */
    @Override
    public List<String> lrangeList(String prefix, String key, int start, int stop) {
        String key_ = getKey(prefix, key);
        try{
            return jedisCluster.lrange(key_, start, stop);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * list类型llen操作
     *
     * @param prefix
     * @param key
     * @return
     */
    @Override
    public Long llenList(String prefix, String key) {
        String key_ = getKey(prefix, key);
        try{
            return jedisCluster.llen(key_);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * set类型add操作
     *
     * @param prefix
     * @param key
     * @param expire
     * @param values
     */
    @Override
    public void sadd(String prefix, String key, Long expire, String... values) {
        String key_ = getKey(prefix, key);
        try{
            jedisCluster.sadd(key_,values);
            jedisExpire(jedisCluster,key_,expire);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
        }
    }

    /**
     * set类型获取集合所有成员操作
     *
     * @param prefix
     * @param key
     * @return
     */
    @Override
    public Set<String> smembers(String prefix, String key) {
        String key_ = getKey(prefix, key);
        try{
            return jedisCluster.smembers(key_);
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * set类型求交集
     *
     * @param prefix
     * @param keys
     * @return
     */
    @Override
    public Set<String> sinter(String prefix, String... keys) {
        try{
            return jedisCluster.sinter(getBatchKey(prefix,keys));
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * set类型求并集
     *
     * @param prefix
     * @param keys
     * @return
     */
    @Override
    public Set<String> sunion(String prefix, String... keys) {
        try{
            return jedisCluster.sunion(getBatchKey(prefix,keys));
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return null;
        }
    }

    /**
     * incr操作
     * <p>
     * 序列服务器
     *
     * @param prefix
     * @param key
     * @return
     */
    @Override
    public long incr(String prefix, String key) {
        try{
            return jedisCluster.incr(getKey(prefix,key));
        } catch (Exception e){
            log.info("redis异常:{}", e.getMessage());
            return 0;
        }
    }
}
