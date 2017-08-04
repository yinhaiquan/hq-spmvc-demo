package hq.com.redis.client.cluster;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import hq.com.aop.utils.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @title : redis cluster 集群连接池配置
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/6/13 10:38 星期二
 */
public class JedisClusterFactory implements FactoryBean<JedisCluster>, InitializingBean {
    private static Logger log = LoggerFactory.getLogger(JedisClusterFactory.class);

    private Properties addressProperty;
    private String addressKeyPrefix;
    private JedisCluster jedisCluster;
    private Integer timeout;
    private Integer maxRedirections;
    private GenericObjectPoolConfig genericObjectPoolConfig;
    private Pattern p = Pattern.compile("^.+[:]\\d{1,5}\\s*$");

    @Override
    public JedisCluster getObject() throws Exception {
        return jedisCluster;
    }

    @Override
    public Class<?> getObjectType() {
        return (this.jedisCluster != null ? this.jedisCluster.getClass() : JedisCluster.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Set<HostAndPort> haps = this.parseHostAndPort();
        jedisCluster = new JedisCluster(haps, timeout, maxRedirections, genericObjectPoolConfig);
        Map<String, JedisPool> map = jedisCluster.getClusterNodes();
        System.out.println("*************集群节点信息*******************");
        log.info("*************集群节点信息*******************");
        Set<String> set = map.keySet();
        for (String ip:set) {
            log.info("{}:{}",ip,map.get(ip));
            System.out.println(StringUtils.format("{0}:{1}",ip, map.get(ip)));
        }
    }

    private Set<HostAndPort> parseHostAndPort() throws Exception {
        try {
            Set<HostAndPort> haps = new HashSet<HostAndPort>();
            for (Object key : addressProperty.keySet()) {
                if (!((String) key).startsWith(addressKeyPrefix)) {
                    continue;
                }
                String val = (String) addressProperty.get(key);
                boolean isIpPort = p.matcher(val).matches();
                if (!isIpPort) {
                    log.info("redis 集群ip或端口书写不合法 eg:ip:port");
                    throw new IllegalArgumentException("ip或port不合法");
                }
                String[] ipAndPort = val.split(":");
                HostAndPort hap = new HostAndPort(ipAndPort[0], Integer.parseInt(ipAndPort[1]));
                haps.add(hap);
            }

            return haps;
        } catch (IllegalArgumentException ex) {
            throw ex;
        } catch (Exception ex) {
            log.info("解析redis ip地址和端口配置文件异常:{}", ex.getMessage());
            throw new Exception("解析jedis配置文件失败", ex);
        }
    }

    public String getAddressKeyPrefix() {
        return addressKeyPrefix;
    }

    public void setAddressKeyPrefix(String addressKeyPrefix) {
        this.addressKeyPrefix = addressKeyPrefix;
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public Integer getMaxRedirections() {
        return maxRedirections;
    }

    public void setMaxRedirections(Integer maxRedirections) {
        this.maxRedirections = maxRedirections;
    }

    public GenericObjectPoolConfig getGenericObjectPoolConfig() {
        return genericObjectPoolConfig;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public Properties getAddressProperty() {
        return addressProperty;
    }

    public void setAddressProperty(Properties addressProperty) {
        this.addressProperty = addressProperty;
    }
}
