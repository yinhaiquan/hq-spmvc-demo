package hq.com.matcher;

import hq.com.aop.utils.StringUtils;
import hq.com.base.RedisManager;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

/**
 * @Describle: 自定义匹配器
 * <p>
 * 限制登录次数
 * </p>
 * @Author: YinHq
 * @Date: Created By 下午 2:44 2017/6/4 0004
 * @Modified By
 */
public class LimitRetryHashedMatcher extends HashedCredentialsMatcher {
    private final String preKey = "shiro_limit_cache_matcher:";
    private RedisManager redisManager;
    //锁定账号时长(单位s)，默认30分钟
    private int expire = 30 * 60;
    //允许登录失败次数，默认5次
    private int times = 5;

    public LimitRetryHashedMatcher(RedisManager redisManager, int expire, int times) {
        this.redisManager = redisManager;
        this.expire = expire;
        this.times = times;
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        String username = (String) token.getPrincipal();
        StringBuffer sb = new StringBuffer(this.preKey);
        sb.append(username);
        String limitTimes = redisManager.get(sb.toString());
        int limit = 0;
        if (StringUtils.isEmpty(limitTimes)) {
            redisManager.set(sb.toString(), "1", expire);
        } else {
            limit = 1 + Integer.parseInt(limitTimes);
            redisManager.set(sb.toString(), String.valueOf(limit), expire);
        }

        if (limit > times) {
            throw new ExcessiveAttemptsException(StringUtils.format("您已登录{0}失败了,请{1}分钟后再试!", times, expire / 60));
        }
        if (null != info) {
            //登录成功，清除缓存
            redisManager.remve(sb.toString());
        }
        return true;
    }

    public String getPreKey() {
        return preKey;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
