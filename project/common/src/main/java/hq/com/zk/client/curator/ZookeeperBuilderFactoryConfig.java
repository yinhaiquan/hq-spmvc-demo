package hq.com.zk.client.curator;

import hq.com.aop.utils.StringUtils;
import org.apache.curator.framework.AuthInfo;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.ACLProvider;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.beans.factory.FactoryBean;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @title : zk客户端配置信息
 * @describle :
 * <p>
 * <b>note:</b>
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * 1. Curator内部实现的几种重试策略:
 *      ExponentialBackoffRetry:重试指定的次数, 且每一次重试之间停顿的时间逐渐增加.
 *      RetryNTimes:指定最大重试次数的重试策略
 *      RetryOneTime:仅重试一次
 *      RetryUntilElapsed:一直重试直到达到规定的时间
 *      <p>
 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
 * 2. Versions:(Curator版本跟zk版本差异)
 *      The are currently two released versions of Curator, 2.x.x and 3.x.x:
 *      <p>
 *      Curator 2.x.x - compatible with both ZooKeeper 3.4.x and ZooKeeper 3.5.x
 *      Curator 3.x.x - compatible only with ZooKeeper 3.5.x and includes support for new features such as dynamic reconfiguration, etc.
 *      <p>
 * >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
 * 3. 每个节点被称为znode, 为znode节点依据其特性, 又可以分为如下类型:
 * 　   PERSISTENT: 永久节点
 * 　   EPHEMERAL: 临时节点, 会随session(client disconnect)的消失而消失
 * 　   PERSISTENT_SEQUENTIAL: 永久节点, 其节点的名字编号是单调递增的(即节点后面会加上递增数值)
 *      EPHEMERAL_SEQUENTIAL: 临时节点, 其节点的名字编号是单调递增的(即节点后面会加上递增数值)
 * <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
 *  4. Zookeeper提供了几种认证方式:
 *      1. world：有个单一的ID，anyone，表示任何人。
 *      2. auth：不使用任何ID，表示任何通过验证的用户（是通过ZK验证的用户？连接到此ZK服务器的用户？）。
 *      3. digest：使用 (用户名：密码) 字符串生成MD5哈希值作为ACL标识符ID。权限的验证通过直接发送用户名密码字符串的方式完成，
 *      4. ip：使用客户端主机ip地址作为一个ACL标识符，ACL表达式是以 addr/bits 这种格式表示的。ZK服务器会将addr的前bits位与客户端地址的前bits位来进行匹配验证权限。
 *
 * Create By yinhaiquan
 * @date 2017/7/7 9:44 星期五
 */
public class ZookeeperBuilderFactoryConfig implements FactoryBean<CuratorFrameworkFactory.Builder>{
    /**zk访问地址*/
    private String ips;
    /**连接超时时间 默认6s*/
    private int connectionTimeoutMs = 6000;
    /**session超时时间 默认6s*/
    private int sessionTimeoutMs = 6000;
    private boolean canBeReadOnly = false;
    /**第一次重试等待时间 默认6s*/
    private int baseSleepTimesMs = 6000;
    /**重试指定的次数, 且每一次重试之间停顿的时间逐渐增加 默认3次*/
    private int maxRetries = 3;
    /**命名空间(后续节点自动新增至此命名空间中，切记：namespace不能用root字符串，否则抛异常)*/
    private String namespace = "hq";
    /**Zk认证方式，默认digest方式*/
    private String scheme = "digest";
    /**zk权限用户名和密码组(用户名:密码)*/
    private String[] username_password;
    /**是否启用zk权限认证，默认不启用*/
    private boolean isAuth = false;
    /**默认命名空间*/
    private final static String ROOT = "haiquan";

    @Override
    public CuratorFrameworkFactory.Builder getObject() throws Exception {
        return bulid();
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFrameworkFactory.Builder.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


    /**
     * 建立zk连接
     *
     * @descripted:
     * <p>
     *     <b>note:</b>
     *
     * </p>
     */
    private CuratorFrameworkFactory.Builder bulid() throws Exception {
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        List<AuthInfo> authInfos = new ArrayList<>();
        builder.connectString(ips)
                .sessionTimeoutMs(sessionTimeoutMs)
                .connectionTimeoutMs(connectionTimeoutMs)
                .canBeReadOnly(canBeReadOnly)
                .retryPolicy(new ExponentialBackoffRetry(baseSleepTimesMs, maxRetries))
                .namespace(StringUtils.isNotEmpty(namespace)?namespace:ROOT)
                .defaultData(null);
        if (isAuth){
            /**开启zk权限认证，指定用户该命名空间下拥有操作权限*/
            if (StringUtils.isNotEmpty(username_password)){
                for (String ippassword : username_password) {
                    AuthInfo authInfo = new AuthInfo(scheme,ippassword.getBytes());
                    authInfos.add(authInfo);
                }
            }
            builder.aclProvider(new ACLAuth())
                    .authorization(authInfos);
        }
        return builder;
    }

    /**
     * zk用户登录权限认证信息
     */
    private class ACLAuth implements ACLProvider{
        private List<ACL> acl;

        @Override
        public List<ACL> getDefaultAcl() {
            if (StringUtils.isEmpty(acl)){
                ArrayList<ACL> acl = ZooDefs.Ids.CREATOR_ALL_ACL;
                acl.clear();
                if (StringUtils.isNotEmpty(username_password)){
                    try {
                        for (String ippassword:username_password) {
                            Id id = new Id(scheme,DigestAuthenticationProvider.generateDigest(ippassword));
                            acl.add(new ACL(ZooDefs.Perms.ALL,id));
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
                this.acl = acl;
            }
            return acl;
        }

        @Override
        public List<ACL> getAclForPath(String s) {
            return acl;
        }
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public int getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(int connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public int getSessionTimeoutMs() {
        return sessionTimeoutMs;
    }

    public void setSessionTimeoutMs(int sessionTimeoutMs) {
        this.sessionTimeoutMs = sessionTimeoutMs;
    }

    public boolean isCanBeReadOnly() {
        return canBeReadOnly;
    }

    public void setCanBeReadOnly(boolean canBeReadOnly) {
        this.canBeReadOnly = canBeReadOnly;
    }

    public int getBaseSleepTimesMs() {
        return baseSleepTimesMs;
    }

    public void setBaseSleepTimesMs(int baseSleepTimesMs) {
        this.baseSleepTimesMs = baseSleepTimesMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String[] getUsername_password() {
        return username_password;
    }

    public void setUsername_password(String[] username_password) {
        this.username_password = username_password;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }
}
