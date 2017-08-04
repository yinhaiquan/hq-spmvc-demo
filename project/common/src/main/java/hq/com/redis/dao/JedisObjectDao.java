package hq.com.redis.dao;

import hq.com.aop.aopenum.RedisDB;

/**
 * @title : redis对象操作API
 * @describle :
 * <p>
 *     <b>note:</b>
 *     1. 主要针对特殊操作，set<T> map<T,K> List<T>。
 *     2. 若需要对Object操作，建议使用jedisDao/jedisClusterDao中setObject方法，效率更高于此API。
 *     3. 对象必须序列化
 *     4. 适用redis单节点以及集群等
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/5 13:53 星期三
 */
public interface JedisObjectDao {
    /**
     * Object类型set操作
     *
     * <p>
     *     若是集群客户端dbindex参数无效，可不填
     * </p>
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @param obj
     * @param expire
     */
    public void setObject(RedisDB dbindex, String prefix, String key, Object obj, Long expire);

    /**
     * Object类型get操作
     *
     * <p>
     *     若是集群客户端dbindex参数无效，可不填
     * </p>
     * @param dbindex DB序号
     * @param prefix  前缀 eg:  test:
     * @param key
     * @return
     */
    public Object getObject(RedisDB dbindex, String prefix, String key);
}
