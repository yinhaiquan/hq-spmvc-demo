package hq.com.zk.dao.impl;

import hq.com.zk.base.ZookeeperTransactionInterface;
import hq.com.zk.dao.ZookeeperTransactionDao;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class ZookeeperTransactionDaoImpl implements ZookeeperTransactionDao {
    private static Logger log = LoggerFactory.getLogger(ZookeeperTransactionDaoImpl.class);
    private CuratorFramework zkClient;

    /**
     * 执行方法
     *
     * @param obj 数据源
     * @param zookeeperTransactionInterface 执行事物接口
     * @return
     */
    @Override
    public Object exec(Object obj, ZookeeperTransactionInterface zookeeperTransactionInterface) {
        CuratorTransaction transaction = zkClient.inTransaction();
        try{
            return zookeeperTransactionInterface.run(transaction,obj);
        } catch (Exception e){
            log.info("执行事物抛出异常:{}",e.getMessage());
        } finally {
            zkClient.close();
        }
        return null;
    }

    public CuratorFramework getZkClient() {
        return zkClient;
    }

    public void setZkClient(CuratorFramework zkClient) {
        this.zkClient = zkClient;
    }
}
