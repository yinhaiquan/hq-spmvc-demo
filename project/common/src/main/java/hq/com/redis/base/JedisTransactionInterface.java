package hq.com.redis.base;

import redis.clients.jedis.Transaction;

/**
 * @title : redis 事物处理抽象类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/6 10:38 星期四
 */
public abstract class JedisTransactionInterface {
    public abstract Object run(Transaction transaction,Object object);
}
