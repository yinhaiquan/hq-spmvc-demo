package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @title :
 * @describle : RMI服务实现
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/21 11:42 星期四
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService {

    protected HelloServiceImpl() throws RemoteException {
    }

    @Override
    public void sayHello(String name) throws RemoteException {
        System.out.println("hello,"+name);
    }
}
