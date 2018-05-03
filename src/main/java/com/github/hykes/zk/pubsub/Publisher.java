package com.github.hykes.zk.pubsub;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/5/3
 */
import java.util.Iterator;
import java.util.List;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionBridge;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher {
    private static final Logger log = LoggerFactory.getLogger(Publisher.class);
    private final PathChildrenCache pcc;
    private final Paths paths;
    private final CuratorFramework client;

    public Publisher(ZKClientFactory zkClientFactory, String topic) throws Exception {
        this(zkClientFactory, "/pubsub", topic);
    }

    public Publisher(ZKClientFactory zkClientFactory, String basePath, String topic) throws Exception {
        this.client = zkClientFactory.getClient();
        this.paths = new Paths(basePath, topic);
        this.pcc = new PathChildrenCache(this.client, this.paths.getSubscriberBase(), false);
        this.pcc.start(StartMode.BUILD_INITIAL_CACHE);
    }

    public void publish(byte[] data) throws Exception {
        List<ChildData> children = this.pcc.getCurrentData();
        if (children.isEmpty()) {
            log.warn("no subscribes for topic:{}", this.paths.getSubscriberBase());
            throw new IllegalStateException("no subscribers exists");
        } else {
            CuratorTransaction t = this.client.inTransaction();

            String targetPath;
            for(Iterator var4 = children.iterator(); var4.hasNext(); t = ((CuratorTransactionBridge)((CuratorTransaction)t).setData().forPath(targetPath, data)).and()) {
                ChildData cd = (ChildData)var4.next();
                String hostName = ZKPaths.getNodeFromPath(cd.getPath());
                targetPath = this.paths.getTopicPathOfHost(hostName);
            }

            if (t instanceof CuratorTransactionFinal) {
                ((CuratorTransactionFinal)t).commit();
            }

        }
    }

    public void destroy() throws Exception {
        this.pcc.close();
    }
}
