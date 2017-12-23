package rmi.zk;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @title : 服务消费者
 * @describle : 基于zk服务注册实现
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/23 11:45 星期六
 */
public class ServiceConsumer {
    /**用于等待 SyncConnected 事件触发后继续执行当前线程*/
    private CountDownLatch latch = new CountDownLatch(1);

    /**定义一个 volatile 成员变量，用于保存最新的 RMI 地址
     * （考虑到该变量或许会被其它线程所修改，一旦修改后，该变量的值会影响到所有线程）
     */
    private volatile List<String> urlList = new ArrayList<>();

    public ServiceConsumer () {
        ZooKeeper zk = connectServer();
        if (null!=zk){
            watchNode(zk);
        }
    }


    public <T extends Remote> T lookup(){
        T service = null;
        int size = urlList.size();
        if (size>0){
            String url;
            if (size==1){
                url = urlList.get(0);
                System.out.println(String.format("获取服务地址:%s",url));
            } else {
                url = urlList.get(ThreadLocalRandom.current().nextInt(size));
                System.out.println(String.format("随机获取服务地址:%s",url));
            }
            service = lookupService(url);
        }
        return service;
    }

    private ZooKeeper connectServer(){
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(Constant.ZK_CONNECTION_STRING, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown(); // 唤醒当前正在执行的线程
                    }
                }
            });
            latch.await(); // 使当前线程处于等待状态
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }

    /**观察 /registry 节点下所有子节点是否有变化*/
    private void watchNode(final ZooKeeper zk){
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType()==Event.EventType.NodeChildrenChanged){
                        watchNode(zk); // 若子节点有变化，则重新调用该方法（为了获取最新子节点中的数据）
                    }
                }
            });
            /**用于存放 /registry 所有子节点中的数据*/
            List<String> dataList = new ArrayList<>();
            for (String node : nodeList) {
                /*// 获取 /registry 的子节点中的数据*/
                byte [] data = zk.getData(Constant.ZK_REGISTRY_PATH+"/" + node, false, null);
                dataList.add(new String(data));
            }
            /**更新最新的 RMI 地址*/
            urlList = dataList;
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private <T> T lookupService(String url){
        T remote = null;
        try {
            remote = (T) Naming.lookup(url);
        } catch (NotBoundException |MalformedURLException |RemoteException e) {
            if (e instanceof ConnectException) {
                // 若连接中断，则使用 urlList 中第一个 RMI 地址来查找（这是一种简单的重试方式，确保不会抛出异常）
                if (urlList.size() != 0) {
                    url = urlList.get(0);
                    return lookupService(url);
                }
            }
        }
        return remote;
    }

}
