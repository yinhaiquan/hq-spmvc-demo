package hq.com.zk.config;

import hq.com.aop.utils.StringUtils;
import hq.com.zk.dao.ZookeeperDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @title : zk配置管理
 *
 * @describle :
 * <p>
 *     <b>note:</b>
 *     分布式系统配置管理协调服务:
 *     1. 使用方式:
 *              配置信息存储在节点数据中，节点path存储在属性文件中。
 *              <bean class="hq.com.zk.config.ZkPropertyPlaceHolderConfigurer">
 *                  <property name="systemPropertiesModeName" value="SYSTEM_PROPERTIES_MODE_OVERRIDE" />
 *                  <property name="ignoreResourceNotFound" value="true" />
 *                  <property name="locations">
 *                              <list>
 *                                      <value>classpath:config/config.properties</value>
 *                              </list>
 *                  </property>
 *                  //zk 客户端API包对应hq.com.zk.dao.impl.ZookeeperDaoImpl，使用时配置即可
 *                  <property name="zookeeperDao" ref="zkDao"/>
 *              </bean>
 *    2. 注意事项：
 *              a. 节点数据格式:key=value
 *              b. 数据模型:
 *                 eg:  zk节点数据 /zoo.path test.prop.id=123456 【zoo.path为节点路径，test.prop.id=123456为数据】
 *                      属性文件配置  test.zk=/zoo.path
 *              c. 支持多个'='，key值为截取第一个'='前面字段
 * </p>
 * Create By yinhaiquan
 * @date 2017/9/4 10:18 星期一
 */
public class ZkPropertyPlaceHolderConfigurer extends PropertyPlaceholderConfigurer{
    private static Logger log = LoggerFactory.getLogger(ZkPropertyPlaceHolderConfigurer.class);

    private ZookeeperDao zookeeperDao;

    /**
     * 装载zk配置管理
     * @param beanFactoryToProcess
     * @param props
     * @throws BeansException
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        super.processProperties(beanFactoryToProcess,props);
        try {
            log.info("启动装载zk配置管理...");
            List<byte []> datas = getData(props);
            fillProperties(props,datas);
            log.info("装载zk配置管理完成...");
        } catch (Exception e) {
            log.info("装载zk配置管理异常:{}",e.getMessage());
        }
    }

    /**
     * 解析zk节点对应的数据
     *
     * @description: 解析通过节点信息获取节点数据，并将节点数据装载入属性对象，供程序访问
     * @param props
     * @param data
     * @throws UnsupportedEncodingException
     */
    private void  fillProperties(Properties props,List<byte []> data) throws UnsupportedEncodingException {
        if (StringUtils.isNotEmpty(data)){
            for (byte [] b: data){
                String item = new String(b,StringUtils.DEFAULT_ENCODING);
                if (StringUtils.isNotEmpty(item)){
                    int index = item.indexOf(StringUtils.EQUAL);
                    if (index!=-1){
                        props.put(item.substring(0,index).trim(),item.substring(index+1).trim());
                    }else{
                        log.info("节点数据[{}]格式不正确!",item);
                    }
                }
            }
        }
    }

    /**
     * 获取属性文件zk节点数据
     *
     * @description: 属性文件存储统一zk节点配置信息，通过节点信息获取节点数据
     * @param props 属性文件对象
     * @return
     * @throws Exception
     */
    private List<byte []> getData(Properties props) throws Exception {
        List<byte []> list = new ArrayList<>();
        Set<Map.Entry<Object,Object>> set = props.entrySet();
        for (Map.Entry<Object,Object> entry: set){
            Object value = entry.getValue();
            //验证节点格式是否正确,此处只处理以'/'开头的数据，默认为节点
            if(StringUtils.isNotEmpty(value)&&String.valueOf(value).startsWith(StringUtils.SLASH)){
                if(zookeeperDao.exists(String.valueOf(value))){
                    list.add(zookeeperDao.getData(String.valueOf(value)).getBytes());
                }else{
                    log.info("属性文件中key={},value={}的节点在zk中未配置.",entry.getKey(),value);
                }
            }else{
                log.info("属性文件中非zk节点配置数据:key={},value={}",entry.getKey(),value);
            }
        }
        return list;
    }

    public ZookeeperDao getZookeeperDao() {
        return zookeeperDao;
    }

    public void setZookeeperDao(ZookeeperDao zookeeperDao) {
        this.zookeeperDao = zookeeperDao;
    }
}
