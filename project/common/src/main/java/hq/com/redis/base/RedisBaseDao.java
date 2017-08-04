package hq.com.redis.base;

import hq.com.aop.aopenum.RedisDB;
import hq.com.aop.utils.StringUtils;
import org.apache.commons.lang.ArrayUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;

/**
 * @title : redis基础包
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/4 11:05 星期二
 */
public class RedisBaseDao {

    protected String getKey(String prefix, String key) {
        StringBuffer sb = new StringBuffer();
        if (StringUtils.isNotEmpty(prefix)) {
            sb.append(prefix);
        }
        sb.append(key);
        return sb.toString();
    }

    protected byte[] getKey(String prefix, byte[] key) {
        if (StringUtils.isNotEmpty(prefix)) {
            return ArrayUtils.addAll(prefix.getBytes(), key);
        } else {
            return key;
        }
    }

    protected String[] getBatchKey(String prefix, String... keys) {
        if (StringUtils.isEmpty(prefix)) {
            return keys;
        } else if (StringUtils.isNotEmpty(keys)) {
            String[] keys_ = new String[keys.length];
            for (int i = 0; i < keys.length; i++) {
                keys_[i] = getKey(prefix, keys[i]);
            }
            return keys_;
        }
        return null;
    }

    protected byte[][] getBatechByteKey(String prefix, byte[]... keys) {
        if (StringUtils.isEmpty(prefix)) {
            return keys;
        } else if (StringUtils.isNotEmpty(keys)) {
            byte[][] keys_ = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++) {
                keys_[i] = getKey(prefix, keys[i]);
            }
            return keys_;
        }
        return null;
    }

    /**
     * 选择DB，默认ODB
     *
     * @param jedis
     * @param dbindex
     */
    protected void selectDB(Jedis jedis, RedisDB dbindex) {
        if (StringUtils.isEmpty(dbindex))
            dbindex = RedisDB.DB0;
        jedis.select(dbindex.getDbIndex());
    }

    /**
     * 设置数据生命周期(单位/s)
     *
     * @param jedis
     * @param key
     * @param expire
     */
    protected void jedisExpire(JedisCommands jedis, String key, Long expire) {
        if (StringUtils.isNotEmpty(expire) && expire.intValue() != -1) {
            jedis.expire(key, expire.intValue());
        }
    }
}
