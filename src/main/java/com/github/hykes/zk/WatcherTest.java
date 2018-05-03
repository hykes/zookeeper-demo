package com.github.hykes.zk;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/5/3
 */
public class WatcherTest implements Watcher {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        String p = "/test";
        ZooKeeper zooKeeper = new ZooKeeper("localhost:2181", 5000, new WatcherTest());
        connectedSemaphore.await();
        //exists register watch
        zooKeeper.exists(p, true);
        String path = zooKeeper.create(p, "456".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        //get register watch
        zooKeeper.getData(path, true, stat);
        zooKeeper.setData(path, "hhhh".getBytes(), -1);
        //exists register watch
        zooKeeper.exists(path, true);
        zooKeeper.delete(path, -1);

    }

    public void process(WatchedEvent event) {
        if (Event.KeeperState.SyncConnected == event.getState()) {
            if (Event.EventType.None == event.getType() && null == event.getPath()) {
                connectedSemaphore.countDown();
                System.out.println("Zookeeper session established");
            } else if (Event.EventType.NodeCreated == event.getType()) {
                System.out.println("success create znode");

            } else if (Event.EventType.NodeDataChanged == event.getType()) {
                System.out.println("success change znode: " + event.getPath());

            } else if (Event.EventType.NodeDeleted == event.getType()) {
                System.out.println("success delete znode");

            } else if (Event.EventType.NodeChildrenChanged == event.getType()) {
                System.out.println("NodeChildrenChanged");
            }
        }
    }
}