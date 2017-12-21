package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * @title :
 * @describle : 基于 JNDI发布RMI 服务(提供者)
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/21 11:44 星期四
 */
public class RMIServer {
    public static void main(String[] args) throws RemoteException, MalformedURLException {
        int port = 1099;
        String url = "rmi://localhost:1099/rmi.HelloServiceImpl";
        LocateRegistry.createRegistry(port);
        Naming.rebind(url,new HelloServiceImpl());
        Naming.rebind(url,new HelloServiceImpl());
    }
}
