package hq.com.listener;

import hq.com.base.RedisManager;
import hq.com.enums.CacheTypeEnums;
import hq.com.session.RedisSessionDao;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import java.io.Serializable;

/**
 * @Describle:
 * @Author: YinHq
 * @Date: Created By 下午 7:06 2017/6/4 0004
 * @Modified By
 */
public class ShiroSessionListener implements SessionListener {

    private RedisManager redisManager;

    public ShiroSessionListener(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    @Override
    public void onStart(Session session) {
    }

    @Override
    public void onStop(Session session) {
        redisManager.del(getByteKey(session.getId()), CacheTypeEnums.SESSIONCACHE);
    }

    @Override
    public void onExpiration(Session session) {
        redisManager.del(getByteKey(session.getId()), CacheTypeEnums.SESSIONCACHE);
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    private byte[] getByteKey(Serializable sessionId) {
        String preKey = RedisSessionDao.keyPrefix + sessionId;
        return preKey.getBytes();
    }
}
