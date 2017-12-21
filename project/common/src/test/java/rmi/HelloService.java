package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @title :
 * @describle : RMI服务接口
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/21 11:40 星期四
 */
public interface HelloService extends Remote {
    void sayHello(String name) throws RemoteException;
}
