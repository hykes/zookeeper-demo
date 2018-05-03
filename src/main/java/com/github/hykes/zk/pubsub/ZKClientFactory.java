package com.github.hykes.zk.pubsub;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/5/3
 */
public class ZKClientFactory {

    private final CuratorFramework client;

    public ZKClientFactory(String zookeeperConnectionString) throws Exception {
        this.client = CuratorFrameworkFactory.newClient(zookeeperConnectionString, new ExponentialBackoffRetry(1000, 3));
        this.client.start();
        this.client.getZookeeperClient().blockUntilConnectedOrTimedOut();
    }

    public CuratorFramework getClient() {
        return this.client;
    }

    public void destroy() {
        this.client.close();
    }
}