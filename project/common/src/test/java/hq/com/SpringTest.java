package hq.com;

import com.google.common.collect.Lists;
import hq.com.aop.exception.ZookeeperLockException;
import hq.com.redis.client.RedisClientTemplate;
import hq.com.redis.dao.JedisClusterDao;
import hq.com.redis.dao.JedisDao;
import hq.com.redis.dao.JedisObjectDao;
import hq.com.redis.dao.JedisTransactionDao;
import hq.com.redis.dao.demo.Test1;
import hq.com.zk.dao.ZookeeperDao;
import hq.com.zk.leader.LeaderSelectorArbitration;
import hq.com.zk.lock.ZookeeperDistributedLock;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.JedisCluster;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * @title :
 * @describle :
 * <p>
 * Create By yinhaiquan
 * @date 2017/5/27 10:51 星期六
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-redis.xml"})
public class SpringTest {

//    @Resource(name = "jedisTemplate")
//    private RedisClientTemplate jedisTemplate;

    @Resource(name = "redisClusterTemplate")
    private RedisClientTemplate clusterTemplate;

    @Resource(name = "jedisDao")
    private JedisDao jedisDao;

    @Resource(name = "jedisClusterDao")
    private JedisClusterDao jedisClusterDao;

    @Resource(name = "jedisObjectDao")
    private JedisObjectDao jedisObjectDao;

    @Resource(name = "jedisTranscationDao")
    private JedisTransactionDao jedisTransactionDao;

    @Resource(name = "zkDao")
    private ZookeeperDao zookeeperDao;

    @Resource(name = "client")
    private CuratorFramework client;

    @Resource(name = "config")
    private CuratorFrameworkFactory.Builder builder;

    @Test
    public void testGlobalProperty() {
//        Jedis jedis = (Jedis)jedisTemplate.getResource();
//        System.out.println(jedis.info());
    }

    @Test
    public void testJedisConcurrent() {
//向JedisPool借用8次连接，但是没有执行归还操作。

//        for (int i = 0; i < 8; i++) {
//            Jedis jedis = null;
//            try {
//                jedis = (Jedis) jedisTemplate.getResource();
//                jedis.ping();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        ((Jedis)jedisTemplate.getResource()).ping();
    }

    @Test
    public void testCluster() {
        JedisCluster jc = (JedisCluster) clusterTemplate.getResource();
        jc.set("fuck", "123");
//        clusterTemplate.getResource().expire("fuck", 10);
        String str = clusterTemplate.getResource().get("fuck");
        System.out.println(str);
//        jedisClusterDao.setString(null, "ksdf", "123456", null);
    }


    @Test
    public void testRedis() {
        Test1 test1 = new Test1();
        test1.setAge(12);
        test1.setDesc("sdfsdfsdfsdf是打发斯蒂芬");
        test1.setName("123sldkfjs234");
//        jedisDao.setObject(RedisDB.DB0, "fk:", "obj2", test1, new Long(-1), Test2.class);
//
//        jedisDao.delBytes(RedisDB.DB0, "fk:", new byte[][]{"obj2".getBytes(), "obj".getBytes()});
//
//        Object obj = jedisDao.getObject(RedisDB.DB0, "fk:", "obj", Test2.class);
//        System.out.println(((Test2) obj).getDesc());
//
//        Map<String, String> map = new HashMap<>();
//        map.put("fk", "dsf");
//        map.put("hh", "123");
//        jedisDao.setHash(RedisDB.DB0, "hash:", "h1", map, null);
//
//        System.out.println(jedisDao.getHashAll(RedisDB.DB0, "hash:", "h1"));
//
//        jedisDao.updateHashField(RedisDB.DB0, "hash:", "h1", "fk", "00000000");
//        System.out.println(jedisDao.getHashFieldValue(RedisDB.DB0, "hash:", "h1", "fk"));
//        System.out.println(jedisDao.getHashAll(RedisDB.DB0, "hash:", "h1"));
//
//        jedisDao.setString(RedisDB.DB0, "str:", "h1", "fk", null);
//        jedisDao.setString(RedisDB.DB0, "str:", "h2", "fk", null);
//
//        jedisDao.delString(RedisDB.DB0, "str:", new String[]{"h1", "h2"});
//        List<String> list = new ArrayList<>();
//        list.add("123");
//        list.add("sdf234");
//
//        jedisDao.lpushList(RedisDB.DB0, null, "list", list, null);
//        System.out.println(jedisDao.lrangeList(RedisDB.DB0, null, "list", 0, -1));
//        System.out.println(jedisDao.llenList(RedisDB.DB0, null, "list"));

//        jedisDao.sadd(RedisDB.DB0, null, "set1", null, new String[]{"sdf", "124123", "sdf", "sdfsdf"});
//        System.out.println(jedisDao.smembers(RedisDB.DB0, null, "set1"));
//        jedisDao.sadd(RedisDB.DB0, null, "set2", null, new String[]{"sdf", "12412123", "sdf", "sdfsdf"});
//        System.out.println(jedisDao.smembers(RedisDB.DB0, null, "set2"));
//        System.out.println(jedisDao.sinter(RedisDB.DB0, null, new String[]{"set1", "set2"}));
//        System.out.println(jedisDao.sunion(RedisDB.DB0, null, new String[]{"set1", "set2"}));
        List<Object> list = new ArrayList<>();
        list.add(test1);
        list.add("sdf");
    }


    @Test
    public void testjedisClusterDao() {
//        jedisClusterDao.setString(null,"ksdf","123456",null);
//        jedisClusterDao.setString(null,"ksdf2","123456",null);
//        jedisClusterDao.setString(null,"ksdf3","123456",null);
        int i = 0;
        while (i < 30) {
            ++i;
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    jedisClusterDao.setString(null, "k" + Math.random() * 100, "1234jgfjfgjf56", null);
                }
            }).start();

        }
    }

    @Test
    public void testJedisObjectDao() {
        Test1 test1 = new Test1();
        test1.setAge(12);
        test1.setDesc("sdfsdfsdfsdf是打发斯蒂芬");
        test1.setName("123sldkfjs234");
        Test1 test2 = new Test1();
        test2.setAge(12);
        test2.setDesc("sdfsdfsdfsdf是打发斯蒂芬");
        test2.setName("123sldkfjs234");
        List<Test1> list = new ArrayList<>();
        list.add(test1);
        list.add(test2);
//        jedisObjectDao.setObject(null, null, "fuck009", list, new Long(20));
//
//        List<Test1> list2 = (List<Test1>) jedisObjectDao.getObject(null, null, "fuck009");
//        System.out.println(list2);
    }

    @Test
    public void testJprotobufRedisObject() {
//        System.out.println(clusterTemplate.getResource().setnx("fk", "fk"));
    }

    @Test
    public void testRedisLock() {
//            RedisDistributedLock lock = new RedisDistributedLock(clusterTemplate, "hq.lock", 1000000);
//            try {
//                if (lock.lock()) {
//
//                }
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } finally {
////                lock.unlock();
//            }
//
//        RedisDistributedLock lock2 = new RedisDistributedLock(clusterTemplate, "hq.lock", 10000);
//        try {
//            if (lock.lock()) {
//
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
////                lock.unlock();
//        }
    }

    @Test
    public void testjedisTransactionDao(){
        System.out.println(jedisDao.getString(null,null,"key_1"));

//        Map<String,String> map = new HashMap<>();
//        map.put("key_1","fuck");
//        map.put("key_2","sdfsdf");
//        jedisTransactionDao.exec(map, new JedisTransactionInterface() {
//            @Override
//            public Object run(Transaction transaction, Object object) {
//                Map<String,String> map = (Map<String, String>) object;
//                System.out.println(map);
//                for (String key : map.keySet()) {
//                   transaction.set(key,map.get(key));
//                }
//                System.out.println("事物提交之前");
//                System.out.println(transaction.get("key_1"));
//                System.out.println(transaction.get("key_2"));
//                return null;
//            }
//        });
    }

    @Test
    public void testzk() throws Exception {
        List<String> list2 =zookeeperDao.getNodes("/");
        System.out.println(list2);
        List<String> list3 =zookeeperDao.getNodes("/");
        System.out.println(list3);
        int i=0;
        while(i<80){
            i++;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        zookeeperDao.createNode("/000000"+Math.random(),true,CreateMode.PERSISTENT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
        Thread.sleep(10000000);
//        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/555");
//        curatorFramework.create().withMode(CreateMode.PERSISTENT).forPath("/555/666", "000".getBytes());
//        byte[] bs = curatorFramework.getData().forPath("/fuck5");
//        System.out.println(new String(bs));
        //获取根下面所有子节点
//        List<String> list2 = curatorFramework.getChildren().forPath("/");
//        System.out.println(list2);
//        curatorFramework.close();
    }

    @Test
    public void testzookeeperDao() throws Exception {

        List<String> list2 =zookeeperDao.getNodes("/");
        System.out.println(list2);
//        zookeeperDao.deleteNode("/567",true);
//        zookeeperDao.deleteAsyncNode("/567");
        zookeeperDao.createNode("////haiquan",true,CreateMode.PERSISTENT);
//        List<String> list2 =curatorFramework.getChildren().forPath("/");
//        System.out.println(list2);
//        curatorFramework.close();

        System.out.println(zookeeperDao.exists("/haiquan"));
//        zookeeperDao.updateData("/567","fuck".getBytes());

//        System.out.println(zookeeperDao.getData("/567"));
    }

    @Test
    public void testthreadZK() throws Exception {
        for (int i =0 ;i<10;i++){
            Thread.sleep(300);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ZookeeperDistributedLock lock = new ZookeeperDistributedLock(client,"zk.lock");
                    try {
                        System.out.println("我是第" + Thread.currentThread().getName() + "号线程，我开始获取锁");
                        System.out.println("我是第" + Thread.currentThread().getName() + "号线程"+lock.getLock().isAcquiredInThisProcess());
                        lock.lock();
                        System.out.println("我是第" + Thread.currentThread().getName() + "号线程，我已经获取锁");
                        System.out.println("我是第" + Thread.currentThread().getName() + "号线程"+lock.getLock().isAcquiredInThisProcess());
                        Thread.sleep(3000);
                    } catch (ZookeeperLockException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            lock.unlock();
                        } catch (ZookeeperLockException e) {
                            e.printStackTrace();
                        }
                    }
                }
            },i+"").start();
        }
        Thread.sleep(100000);
    }

    @Test
    public void testLeaderSelector() throws InterruptedException, IOException {
        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderSelectorArbitration> leaderClients = Lists.newArrayList();
        for (int i =0 ;i<2;i++){
            CuratorFramework zkClient = builder.build();
            zkClient.start();
            clients.add(zkClient);
            LeaderSelectorArbitration leader = new LeaderSelectorArbitration(client,"Client#" + i);
            leader.start();
            leaderClients.add(leader);
        }

        System.out.println("----------先观察一会选举的结果-----------");
        Thread.sleep(10000);

        System.out.println("----------关闭前5个客户端，再观察选举的结果-----------");
        for (int i = 0; i < 2; i++) {
            clients.get(i).close();
        }

        new BufferedReader(new InputStreamReader(System.in)).readLine();
    }
}