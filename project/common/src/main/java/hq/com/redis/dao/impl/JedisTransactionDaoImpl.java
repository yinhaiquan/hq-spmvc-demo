package hq.com.redis.dao.impl;

import hq.com.aop.utils.StringUtils;
import hq.com.redis.client.jedis.JedisTemplate;
import hq.com.redis.dao.JedisTransactionDao;
import hq.com.redis.base.JedisTransactionInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.io.IOException;

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
public class JedisTransactionDaoImpl implements JedisTransactionDao {
    private static Logger log = LoggerFactory.getLogger(JedisTransactionDaoImpl.class);
    private JedisTemplate jedisTemplate;

    public void setJedisTemplate(JedisTemplate jedisTemplate) {
        this.jedisTemplate = jedisTemplate;
    }

    /**
     * 执行方法
     *
     * @param obj 数据包
     * @param jedisTransactionInterface 具体业务实现接口
     * @return
     * @throws IOException
     */
    @Override
    public Object exec(Object obj, JedisTransactionInterface jedisTransactionInterface){
        Jedis jedis = jedisTemplate.getResource();
        Object object = null;
        Transaction transaction = null;
        try {
            transaction = multi(jedis);
            if (StringUtils.isNotEmpty(transaction)){
                object = jedisTransactionInterface.run(transaction,obj);
                transaction.exec();
            }else {
                throw new Exception("获取不到redis事物对象");
            }
        } catch (Exception e) {
            log.info("异常信息:{}",e.getMessage());
            this.rollback(transaction);
        } finally {
            try {
                jedisTemplate.close(jedis);
            } catch (IOException e) {
                log.info("回收redis连接异常信息:{}",e.getMessage());
            }
        }
        return object;
    }

    private Transaction multi(Jedis jedis){
        if (StringUtils.isNotEmpty(jedis)){
            return jedis.multi();
        }
        return null;
    }

    private void rollback(Transaction transaction){
        if (StringUtils.isNotEmpty(transaction)){
            transaction.discard();
        }
    }
}
