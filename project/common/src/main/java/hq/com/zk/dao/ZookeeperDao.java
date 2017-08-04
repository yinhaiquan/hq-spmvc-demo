package hq.com.zk.dao;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * @title : zk客户端API
 * @describle :
 * <p>
 *    每个节点被称为znode, 为znode节点依据其特性, 又可以分为如下类型:
 * 　   PERSISTENT: 永久节点
 * 　   EPHEMERAL: 临时节点, 会随session(client disconnect)的消失而消失
 * 　   PERSISTENT_SEQUENTIAL: 永久节点, 其节点的名字编号是单调递增的(即节点后面会加上递增数值)
 *      EPHEMERAL_SEQUENTIAL: 临时节点, 其节点的名字编号是单调递增的(即节点后面会加上递增数值)
 * Create By yinhaiquan
 * @date 2017/7/6 18:47 星期四
 */
public interface ZookeeperDao {
    /**
     * 创建节点
     *
     * @param node 节点名称
     * @param isCreateParentNode 是否支持创建父级节点
     * @param createMode 节点类型
     * @param data 节点数据
     */
    public void createNode(String node,boolean isCreateParentNode, CreateMode createMode, byte[] data) throws Exception;

    /**
     * 创建节点
     *
     * @param node 节点名称
     * @param isCreateParentNode 是否支持创建父级节点
     * @param createMode 节点类型
     */
    public void createNode(String node,boolean isCreateParentNode, CreateMode createMode) throws Exception;

    /**
     * 获取节点孩子列表
     *
     * @param node 节点名称(若是获取根节点，直接赋值"/")
     * @return
     */
    public List<String> getNodes(String node) throws Exception;

    /**
     * 删除节点
     *
     * @param node 节点名称
     * @param killChildren 是否删除子节点
     */
    public void deleteNode(String node,boolean killChildren) throws Exception;

    /**
     * 异步删除节点
     *
     * @param node 节点名称
     * @param backgroundCallback 异步回调
     */
    public void deleteAsyncNode(String node, BackgroundCallback backgroundCallback,boolean killChildren) throws Exception;

    /**
     * 判断节点是否存在
     *
     * @param node 节点名称
     * @return
     */
    public boolean exists(String node) throws Exception;

    /**
     * 更新节点数据
     *
     * @param node 节点名称
     * @param data 节点数据
     * @throws Exception
     */
    public void updateData(String node,byte[] data) throws Exception;

    /**
     * 异步更新节点数据
     *
     * @param node 节点名称
     * @param data 节点数据
     * @throws Exception
     */
    public void updateAsyncData(String node,byte[] data,BackgroundCallback backgroundCallback) throws Exception;

    /**
     * 获取节点下面数据
     *
     * @param node 节点名称
     * @return
     * @throws Exception
     */
    public String getData(String node) throws Exception;

    /**
     * 获取节点下面所有子节点
     *
     * @param node 节点
     * @return
     * @throws Exception
     */
    public List<String> getChildNodes(String node) throws Exception;
}
