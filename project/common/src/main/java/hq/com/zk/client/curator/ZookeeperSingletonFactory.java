package hq.com.zk.client.curator;

import hq.com.aop.utils.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @title : zk 单例工厂
 * @describle :
 * <p>
 *     <b>note:</b>
 *     本单例工厂只应用于zk分布式锁场景，只为锁开启一个zk连接。
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/11 11:55 星期二
 */
public class ZookeeperSingletonFactory implements FactoryBean<CuratorFramework>,InitializingBean {
    private CuratorFrameworkFactory.Builder builder;
    private CuratorFramework client;

    @Override
    public CuratorFramework getObject() throws Exception {
        if (StringUtils.isEmpty(client)){
            create();
        }
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    private void create(){
        client = builder.build();
        client.start();
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public CuratorFrameworkFactory.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(CuratorFrameworkFactory.Builder builder) {
        this.builder = builder;
    }
}
