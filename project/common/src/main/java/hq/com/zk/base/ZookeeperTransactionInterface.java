package hq.com.zk.base;

import org.apache.curator.framework.api.transaction.CuratorTransaction;

/**
 * @title : zk 事物处理抽象类
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/7/10 16:47 星期一
 */
public abstract class ZookeeperTransactionInterface {
    public abstract Object run(CuratorTransaction transaction, Object object);
}
