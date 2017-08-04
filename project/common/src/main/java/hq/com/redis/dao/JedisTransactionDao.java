package hq.com.redis.dao;

import hq.com.redis.base.JedisTransactionInterface;

/**
 * @title : 单节点 redis 事物操作
 * @describle :
 * <p>
 *     <b>note:</b>
 *     redis 基于事物操作,主要针对特殊复杂业务
 *                multi 开启事务
 *                exec 提交事务
 *                discard 取消事务
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/6 10:24 星期四
 */
public interface JedisTransactionDao {
    public Object exec(Object obj, JedisTransactionInterface jedisTransactionInterface);
}
