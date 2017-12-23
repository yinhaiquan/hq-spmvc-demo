package rmi.zk;

import rmi.HelloService;
import rmi.HelloServiceImpl;

import java.rmi.RemoteException;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/23 13:49 星期六
 */
public class Server {
    public static void main(String[] args) throws RemoteException {
        HelloService helloService = new HelloServiceImpl();
        ServiceProvider provider = new ServiceProvider();
        /**可实现分布式集群部署服务*/
        provider.publish(helloService,"localhost",1099);
        provider.publish(helloService,"localhost",1029);
    }
}
