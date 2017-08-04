package hq.com.cache;

import hq.com.aop.utils.StringUtils;
import hq.com.base.RedisManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Describle: 认证权限缓存管理器
 * @Author: YinHq
 * @Date: Created By 下午 12:35 2017/6/4 0004
 * @Modified By
 */
public class AuthenticationCacheManager implements CacheManager {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ConcurrentMap<String, Cache> caches = new ConcurrentHashMap<String, Cache>();
    private RedisManager redisManager;
    private String keyPrefix = "shiro_redis_cache:";

    public AuthenticationCacheManager(RedisManager redisManager, String keyPrefix) {
        this.redisManager = redisManager;
        this.keyPrefix = keyPrefix;
    }

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        log.info("获取名称为: ${} 的RedisCache实例", s);
        Cache c = caches.get(s);
        if (StringUtils.isEmpty(c)) {
            c = new RedisCache<K, V>(redisManager, keyPrefix);
            caches.put(s, c);
        }
        return c;
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
