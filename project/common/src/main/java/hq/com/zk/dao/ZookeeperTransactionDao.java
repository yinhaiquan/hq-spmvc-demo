package hq.com.zk.dao;

import hq.com.zk.base.ZookeeperTransactionInterface;

/**
 * @title : zk客户端API
 * @describle :
 * <p>
 *     <b>note:</b>
 *     多个zk CRUD 操作，需要借助事物保证数据完整性
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/10 16:33 星期一
 */
public interface ZookeeperTransactionDao {
    public Object exec(Object obj,ZookeeperTransactionInterface zookeeperTransactionInterface);
}
