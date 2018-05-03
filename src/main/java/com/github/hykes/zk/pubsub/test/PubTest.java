package com.github.hykes.zk.pubsub.test;

import com.github.hykes.zk.pubsub.Publisher;
import com.github.hykes.zk.pubsub.ZKClientFactory;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/5/3
 */
public class PubTest {

    public static void main(String[] args) throws Exception {

        ZKClientFactory zkClientFactory = new ZKClientFactory("localhost:2181");

        Publisher publisher = new Publisher(zkClientFactory, "test-topic");

        publisher.publish("test".getBytes());
    }
}
