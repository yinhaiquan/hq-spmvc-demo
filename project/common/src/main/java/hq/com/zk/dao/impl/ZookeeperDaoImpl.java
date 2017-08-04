package hq.com.zk.dao.impl;

import hq.com.aop.utils.StringUtils;
import hq.com.zk.base.ZookeeperBaseDao;
import hq.com.zk.dao.ZookeeperDao;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * @title : zk客户端API
 * @describle : 原子操作
 *
 * <p>
 *    <b>note:</b>
 *    每个节点被称为znode, 为znode节点依据其特性, 又可以分为如下类型:
 * 　   PERSISTENT: 永久节点
 * 　   EPHEMERAL: 临时节点, 会随session(client disconnect)的消失而消失
 * 　   PERSISTENT_SEQUENTIAL: 永久节点, 其节点的名字编号是单调递增的(即节点后面会加上递增数值)
 *      EPHEMERAL_SEQUENTIAL: 临时节点, 其节点的名字编号是单调递增的(即节点后面会加上递增数值)
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/6 18:47 星期四
 */
public class ZookeeperDaoImpl extends ZookeeperBaseDao implements ZookeeperDao {
    private static Logger log = LoggerFactory.getLogger(ZookeeperDaoImpl.class);
    private CuratorFrameworkFactory.Builder builder;

    public ZookeeperDaoImpl() {
    }

    /**
     * 创建节点
     *
     * @param node       节点名称
     * @param isCreateParentNode 是否支持创建父级节点
     * @param createMode 节点类型
     * @param data       节点数据
     */
    @Override
    public void createNode(String node,boolean isCreateParentNode, CreateMode createMode, byte[] data) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        try {
            if (isCreateParentNode){
                zkClient.create().creatingParentsIfNeeded().withMode(createMode).forPath(format(node),data);
            }else{
                zkClient.create().withMode(createMode).forPath(format(node),data);
            }
        } finally {
            close(zkClient);
        }
    }

    /**
     * 创建节点
     *
     * @param node       节点名称
     * @param isCreateParentNode 是否支持创建父级节点
     * @param createMode 节点类型
     */
    @Override
    public void createNode(String node,boolean isCreateParentNode, CreateMode createMode) throws Exception {
        createNode(node,isCreateParentNode,createMode,new byte[]{});
    }

    /**
     * 获取节点孩子列表
     *
     * @param node 节点名称(若是获取根节点，直接赋值"/")
     * @return
     */
    @Override
    public List<String> getNodes(String node) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        List<String> list;
        try {
            list = zkClient.getChildren().forPath(format(node));
        } finally {
            close(zkClient);
        }
        return list;
    }

    /**
     * 删除节点
     * @Describle 若该节点下面包含子节点，则不能删除，并抛出NotEmptyException异常
     *
     * @param node 节点名称
     * @param killChildren 是否删除子节点
     */
    @Override
    public void deleteNode(String node,boolean killChildren) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        try {
            if (killChildren){
                zkClient.delete().deletingChildrenIfNeeded().forPath(format(node));
            }else{
                zkClient.delete().forPath(format(node));
            }
        } finally {
            close(zkClient);
        }
    }

    /**
     * 异步删除节点
     *
     * @Describle:
     * <p>
     *     <b>note:</b>
     *     使用异步操作方式时，注意将异步方法
     *
     * </p>
     * @param node 节点名称
     * @param backgroundCallback 异步回调
     */
    @Override
    public void deleteAsyncNode(String node,BackgroundCallback backgroundCallback,boolean killChildren) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        try {
            if (killChildren){
                zkClient.delete().deletingChildrenIfNeeded().inBackground(backgroundCallback).forPath(format(node));
            }else {
                zkClient.delete().inBackground().forPath(format(node));
            }
        } finally {
            close(zkClient);
        }

    }

    /**
     * 判断节点是否存在
     *
     * @param node
     * @return
     */
    @Override
    public boolean exists(String node) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        try {
            Stat stat = zkClient.checkExists().forPath(format(node));
            if (StringUtils.isEmpty(stat)){
                return false;
            }
        } finally {
            close(zkClient);
        }
        return true;
    }

    /**
     * 更新节点数据
     *
     * @param node 节点名称
     * @param data 节点数据
     * @throws Exception
     */
    @Override
    public void updateData(String node, byte[] data) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        try {
            zkClient.setData().forPath(format(node),data);
        } finally {
            close(zkClient);
        }
    }

    /**
     * 异步更新节点数据
     *
     * @param node 节点名称
     * @param data 节点数据
     * @throws Exception
     */
    @Override
    public void updateAsyncData(String node, byte[] data,BackgroundCallback backgroundCallback) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        try {
            zkClient.setData().inBackground(backgroundCallback).forPath(format(node),data);
        } finally {
            close(zkClient);
        }
    }

    /**
     * 获取节点下面数据
     *
     * @param node 节点名称
     * @return
     * @throws Exception
     */
    @Override
    public String getData(String node) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        try {
            byte[] data = zkClient.getData().forPath(format(node));
            return StringUtils.isNotEmpty(data)?new String(data):null;
        } finally {
            close(zkClient);
        }
    }

    /**
     * 获取节点下面所有子节点
     *
     * @param node 节点
     * @return
     * @throws Exception
     */
    @Override
    public List<String> getChildNodes(String node) throws Exception {
        CuratorFramework zkClient = builder.build();
        zkClient.start();
        try {
           return zkClient.getChildren().forPath(format(node));
        } finally {
            close(zkClient);
        }
    }

    private void close(CuratorFramework curatorFramework){
        if (StringUtils.isNotEmpty(curatorFramework)){
            curatorFramework.close();
        }
    }

    public CuratorFrameworkFactory.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(CuratorFrameworkFactory.Builder builder) {
        this.builder = builder;
    }
}
