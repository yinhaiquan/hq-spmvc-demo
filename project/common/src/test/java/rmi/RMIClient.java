package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * @title :
 * @describle : RMI 客户端(消费者)
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/21 11:48 星期四
 */
public class RMIClient {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        String url = "rmi://localhost:1099/rmi.HelloServiceImpl";
        HelloService helloService = (HelloService) Naming.lookup(url);
        helloService.sayHello("rmi");
    }
}
