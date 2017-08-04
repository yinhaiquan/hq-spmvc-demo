package hq.com.session;

import hq.com.aop.utils.StringUtils;
import hq.com.base.RedisManager;
import hq.com.base.SerializeUtils;
import hq.com.enums.CacheTypeEnums;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Describle: 自定义会话管理
 * <p>
 * 基于redis缓存实现
 * </p>
 * @Author: YinHq
 * @Date: Created By 下午 10:59 2017/6/3 0003
 * @Modified By
 */
public class RedisSessionDao extends AbstractSessionDAO {
    private RedisManager redisManager;
    public static String keyPrefix = "shiro_redis_session:";

    public RedisSessionDao(RedisManager redisManager) {
        this.redisManager = redisManager;
        System.out.println(StringUtils.isNotEmpty(redisManager) ? "redis注入成功!" : "redis注入失败!");
    }

    /**
     * 获得byte[]型的key
     *
     * @param sessionId
     * @return
     */
    private byte[] getByteKey(Serializable sessionId) {
        String preKey = keyPrefix + sessionId;
        return preKey.getBytes();
    }

    @Override
    protected Serializable doCreate(Session session) {
        try {
            Serializable sessionId = this.generateSessionId(session);
            this.assignSessionId(session, sessionId);
            this.saveSession(session);
            return sessionId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    private void saveSession(Session session) throws UnknownSessionException {
        try {
            if (session == null || session.getId() == null) {
                return;
            }

            byte[] key = getByteKey(session.getId());
            byte[] value = SerializeUtils.serialize(session);
            session.setTimeout(redisManager.getExpire());
            this.redisManager.set(key, value, redisManager.getExpire());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected Session doReadSession(Serializable serializable) {
        try {
            if (serializable == null) {
                return null;
            }
            Session s = null;
            byte [] value = redisManager.get(this.getByteKey(serializable), CacheTypeEnums.SESSIONCACHE);
            if (StringUtils.isNotEmpty(value)){
                s = (Session) SerializeUtils.deserialize(value);
            }

            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        try {
            this.saveSession(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null) {
            return;
        }
        redisManager.del(this.getByteKey(session.getId()), CacheTypeEnums.SESSIONCACHE);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<Session> sessions = new HashSet<Session>();

        Set<byte[]> keys = redisManager.keys(this.keyPrefix + "*");
        if (keys != null && keys.size() > 0) {
            for (byte[] key : keys) {
                Session s = (Session) SerializeUtils.deserialize(redisManager.get(key, CacheTypeEnums.SESSIONCACHE));
                if (StringUtils.isNotEmpty(s))
                    sessions.add(s);
            }
        }

        return sessions;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }
}
