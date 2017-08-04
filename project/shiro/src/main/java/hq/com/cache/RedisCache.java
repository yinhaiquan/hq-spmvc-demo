package hq.com.cache;

import hq.com.aop.utils.StringUtils;
import hq.com.base.RedisManager;
import hq.com.base.SerializeUtils;
import hq.com.enums.CacheTypeEnums;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 1:00 2017/6/4 0004
 * @Modified By
 */
public class RedisCache<K, V> implements Cache<K, V> {
    private Logger log = LoggerFactory.getLogger(this.getClass());

    private RedisManager redisManager;
    private String keyPrefix = "shiro_redis_cache:";

    public RedisCache(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public RedisCache(RedisManager redisManager, String keyPrefix) {
        this.redisManager = redisManager;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public V get(K k) throws CacheException {
        log.info("根据key从Redis中获取对象 key [{}]", k);
        if (StringUtils.isEmpty(k)) {
            return null;
        } else {
            byte[] rawvalue = redisManager.get(getByteKey(k), CacheTypeEnums.AUTHORIZATIONCACHE);
            V value = (V) SerializeUtils.deserialize(rawvalue);
            return value;
        }
    }

    @Override
    public V put(K k, V v) throws CacheException {
        log.info("根据key从存储 key [{}]", k);
        redisManager.set(getByteKey(k), SerializeUtils.serialize(v));
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        log.info("从redis中删除 key [{}]", k);
        V previous = get(k);
        redisManager.del(getByteKey(k), CacheTypeEnums.AUTHORIZATIONCACHE);
        return previous;
    }

    @Override
    public void clear() throws CacheException {
        log.info("从redis中删除所有缓存，默认DB0库");
        redisManager.flushDB();
    }

    @Override
    public int size() {
        Long size = redisManager.dbSize();
        log.info("获取shiro在redis中缓存大小:{}", size);
        return size.intValue();
    }

    @Override
    public Set<K> keys() {
        try {
            Set<byte[]> keys = redisManager.keys(this.keyPrefix + "*");
            if (CollectionUtils.isEmpty(keys)) {
                return Collections.emptySet();
            } else {
                Set<K> newKeys = new HashSet<K>();
                for (byte[] key : keys) {
                    newKeys.add((K) key);
                }
                return newKeys;
            }
        } catch (Throwable e) {
            throw new CacheException(e);
        }
    }

    @Override
    public Collection<V> values() {
        try {
            Set<byte[]> keys = redisManager.keys(this.keyPrefix + "*");
            if (!CollectionUtils.isEmpty(keys)) {
                List<V> values = new ArrayList<V>(keys.size());
                for (byte[] key : keys) {
                    V value = get((K) key);
                    if (value != null) {
                        values.add(value);
                    }
                }
                return Collections.unmodifiableList(values);
            } else {
                return Collections.emptyList();
            }
        } catch (Throwable t) {
            throw new CacheException(t);
        }
    }


    /**
     * 转换字节数组
     *
     * @param key
     * @return
     */
    private byte[] getByteKey(K key) {
        if (key instanceof String) {
            String preKey = this.keyPrefix + key;
            return preKey.getBytes();
        } else {
            return byteMerger(this.keyPrefix.getBytes(), SerializeUtils.serialize(key));
        }
    }

    /**
     * 合并字节数组
     *
     * @param byte_1
     * @param byte_2
     * @return
     */
    private byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public String getKeyPrefix() {
        return keyPrefix;
    }

    public void setKeyPrefix(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }
}
