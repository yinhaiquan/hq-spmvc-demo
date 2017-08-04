package hq.com.zk.leader;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @title : zk 选举仲裁客户端
 * @describle : 本选举仲裁客户单基于leaderSelector实现，所有存活的客户端公平轮流做leader
 *              如果不想频繁的变化Leader，需要在takeLeadership方法里阻塞leader的变更
 * <p>
 *     <b>note:</b>
 *     1. 选举主要依赖于LeaderSelector和LeaderLatch2个类
 *        a. 前者是所有存活的客户端不间断的公平轮流做Leader
 *        b. 后者是一旦选举出Leader，除非有客户端挂掉重新触发选举，否则不会交出领导权
 * </p>
 * Create By yinhaiquan
 * @date 2017/7/11 14:59 星期二
 */
public class LeaderSelectorArbitration extends LeaderSelectorListenerAdapter implements Closeable{
    private final String name;
    private final LeaderSelector leaderSelector;
    private final String path = "/leadernode";

    public LeaderSelectorArbitration(CuratorFramework client,String name) {
        this.name = name;
        this.leaderSelector = new LeaderSelector(client,path,this);
        this.leaderSelector.autoRequeue();
    }


    public void start(){
        leaderSelector.start();
    }

    @Override
    public void close() throws IOException {
        leaderSelector.close();
    }

    /**
     * 经过仲裁选举出的leader会调用此方法
     * @param client
     * @throws Exception
     */
    @Override
    public void takeLeadership(CuratorFramework client) throws Exception {
        int waitSeconds = (int) (5 * Math.random()) + 1;
        System.out.println(name+"是当前的leader");
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public String getName() {
        return name;
    }

    public LeaderSelector getLeaderSelector() {
        return leaderSelector;
    }

    public String getPath() {
        return path;
    }
}
