package jmx;

import com.sun.jdmk.comm.HtmlAdaptorServer;

import javax.management.*;
import javax.management.remote.*;
import javax.management.remote.rmi.RMIConnectorServer;
import javax.security.auth.Subject;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RMISocketFactory;
import java.util.*;

/**
 * @title : JMX监控
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/12/14 13:45 星期四
 */
public class HelloAgent {

    /**
     * html web访问方式
     * http://127.0.0.1:8080
     * @throws MalformedObjectNameException
     * @throws NotCompliantMBeanException
     * @throws InstanceAlreadyExistsException
     * @throws MBeanRegistrationException
     */
    public static void showHtml() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer server = MBeanServerFactory.createMBeanServer("HelloAgent");
        ObjectName helloName = new ObjectName("HelloAgent:name=HelloWorld");
        server.registerMBean(new Hello(),helloName);
        HtmlAdaptorServer htmlAdaptorServer = new HtmlAdaptorServer();
        ObjectName adpaterName = new ObjectName("HelloAgent:name=htmladapter,port=8089");
        htmlAdaptorServer.setPort(8080);//可以不设置，若设置了，以此处端口生效
        server.registerMBean(htmlAdaptorServer,adpaterName);
        htmlAdaptorServer.start();
    }

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, IOException, InterruptedException {
//        showHtml();
        showJconsole();
//        testNotify();
        System.out.println("running.....");
    }

    public static void testNotify() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName helloName = new ObjectName("fuck:name=fuck");
        Hello hello = new Hello();
        server.registerMBean(hello,helloName);
        Jack jack = new Jack();
        server.registerMBean(jack,new ObjectName("jack:name=Jack"));
        jack.addNotificationListener(new HelloListener(),null,hello);
        Thread.sleep(1000000);
    }

    /**
     * java jconsole.jar执行方式
     *
     * 127.0.0.1:8080 admin/admin
     *
     * @throws IOException
     * @throws MalformedObjectNameException
     * @throws NotCompliantMBeanException
     * @throws InstanceAlreadyExistsException
     * @throws MBeanRegistrationException
     */
    public static void showJconsole() throws IOException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        String addr = "127.0.0.1:8087";
        JMXServiceURL address = new JMXServiceURL("service:jmx:rmi://"+addr+ "/jndi/rmi://" + addr + "/jmxrmi");

        Map<String,Object> jmxEnvironment = new HashMap<>();
        jmxEnvironment.put("jmx.remote.credentials",new String[] { "admin",
                "admin" });

        InetAddress ipInterface = InetAddress.getByName("127.0.0.1");

        RMISocketFactory rmiSocketFactory = RMISocketFactory.getDefaultSocketFactory();
        //RMISocketFactory rmiSocketFactory = new AnchorSocketFactory(ipInterface,"false");
        LocateRegistry.createRegistry(8087,null,rmiSocketFactory);
        jmxEnvironment.put(RMIConnectorServer.RMI_SERVER_SOCKET_FACTORY_ATTRIBUTE,rmiSocketFactory);


        // 需要认证则这么写：
        JMXAuthenticator auth = createJMXAuthenticator();
        jmxEnvironment.put(JMXConnectorServer.AUTHENTICATOR, auth);
        JMXConnectorServer connector = JMXConnectorServerFactory.newJMXConnectorServer(address,jmxEnvironment,ManagementFactory.getPlatformMBeanServer());
        HelloMBean mBean = new Hello();
        ObjectName mBeanName = new ObjectName("fuckMbean:name=fuck");
        mbs.registerMBean(mBean,mBeanName);
        connector.start();
    }

    /**
     * 权限认证
     * @return
     */
    private static JMXAuthenticator createJMXAuthenticator() {
        return new JMXAuthenticator() {
            public Subject authenticate(Object credentials) {
                String[] sCredentials = (String[]) credentials;
                if (null == sCredentials || sCredentials.length != 2) {
                    throw new SecurityException("Authentication failed!");
                }
                String userName = sCredentials[0];
                String pValue = sCredentials[1];
                if ("admin".equals(userName) && "admin".equals(pValue)) {
                    Set principals = new HashSet();
                    principals.add(new JMXPrincipal(userName));
                    return new Subject(true, principals, Collections.EMPTY_SET,
                            Collections.EMPTY_SET);
                }
                throw new SecurityException("Authentication failed!");
            }
        };
    }
}
