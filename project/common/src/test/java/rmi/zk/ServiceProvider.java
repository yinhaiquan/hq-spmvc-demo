package rmi.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.CountDownLatch;

/**
 * @title : 服务提供者
 * @describle : 基于zk服务注册实现
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/23 10:49 星期六
 */
public class ServiceProvider {
    /** 用于等待syncConneted 事件触发后继续执行当前线程 */
    private CountDownLatch latch = new CountDownLatch(1);

    /** 发布RMI服务并注册RMI地址至zk */
    public void publish(Remote remote,String host,int port){
        String url = publishService(remote, host, port);
        if (null!=url){
            ZooKeeper zk = connectServer();
            if (null!=zk){
                createNode(zk,url);
            }
        }
    }

    private String publishService(Remote remote, String host, int port){
        String url = null;
        try {
            System.out.println(String.format("发布服务:%s",remote.getClass().getName()));
            System.out.println("发布中。。。");
            url = String.format("rmi://%s:%d/%s",host,port,remote.getClass().getName());
            LocateRegistry.createRegistry(port);
            Naming.rebind(url,remote);
            System.out.println(String.format("发布成功:%s",remote.getClass().getName()));
        } catch (RemoteException | MalformedURLException e){
            System.out.println("服务发布失败!");
            System.out.println(e.getMessage());
        }
        return url;
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

    private void createNode(ZooKeeper zk, String url){
        byte [] data = url.getBytes();
        try {
            Stat stat = zk.exists(Constant.ZK_REGISTRY_PATH,false);
            if (null==stat){
                String u = zk.create(Constant.ZK_REGISTRY_PATH,null,ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
                System.out.println(u);
            }

            String path = zk.create(Constant.ZK_PROVIDER_PATH,data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);    // 创建一个临时性且有序的 ZNode
            System.out.println(String.format("创建节点:<%s> data:%s",path,url));
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
