package com.github.hykes.zk.pubsub;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.utils.ZKPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/5/3
 */
public class Cleaner {

    private static final Logger log = LoggerFactory.getLogger(Cleaner.class);
    private final PathChildrenCache liveHostsPcc;
    private final PathChildrenCache topicPcc;
    private final Paths paths;
    private final CuratorFramework client;

    public Cleaner(ZKClientFactory zkClientFactory, String topic) throws Exception {
        this(zkClientFactory, "/pubsub", topic);
    }

    public Cleaner(ZKClientFactory zkClientFactory, String basePath, String topic) throws Exception {
        this.client = zkClientFactory.getClient();
        this.paths = new Paths(basePath, topic);
        this.liveHostsPcc = new PathChildrenCache(this.client, this.paths.getClientBase(), false);
        this.liveHostsPcc.start(StartMode.BUILD_INITIAL_CACHE);
        this.topicPcc = new PathChildrenCache(this.client, this.paths.getTopicBase(), false);
        this.topicPcc.start(StartMode.BUILD_INITIAL_CACHE);
    }

    public void clean() throws Exception {
        List<ChildData> liveHostsChildData = this.liveHostsPcc.getCurrentData();
        if (liveHostsChildData.isEmpty()) {
            log.info("no client exists for path: {}", this.paths.getClientBase());
        } else {
            List<String> liveHosts = Lists.transform(liveHostsChildData, it -> ZKPaths.getNodeFromPath(it.getPath()));

            List<ChildData> topicChildData = this.topicPcc.getCurrentData();
            if (topicChildData.isEmpty()) {
                log.info("no topic exists for path: {}", this.paths.getTopicBase());
            } else {
                Iterator var4 = topicChildData.iterator();

                while(var4.hasNext()) {
                    ChildData child = (ChildData)var4.next();
                    String hostName = ZKPaths.getNodeFromPath(child.getPath());
                    if (!liveHosts.contains(hostName)) {
                        this.client.delete().forPath(this.paths.getTopicPathOfHost(hostName));
                    }
                }
            }
        }
    }
}
