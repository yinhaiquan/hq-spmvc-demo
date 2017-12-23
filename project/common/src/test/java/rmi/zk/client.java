package rmi.zk;

import rmi.HelloService;

import java.rmi.RemoteException;

/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/23 13:49 星期六
 */
public class client {
    public static void main(String[] args) throws RemoteException, InterruptedException {
        ServiceConsumer sc = new ServiceConsumer();

        while(true){
            HelloService helloService = sc.lookup();
            if (null==helloService){
                System.out.println("注册中心无对应的服务!");
            }else{
                helloService.sayHello("fuck you");
            }
            Thread.sleep(3*1000);
        }
    }
}
