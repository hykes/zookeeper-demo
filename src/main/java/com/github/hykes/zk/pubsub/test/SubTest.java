package com.github.hykes.zk.pubsub.test;

import com.github.hykes.zk.pubsub.Subscriber;
import com.github.hykes.zk.pubsub.ZKClientFactory;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/5/3
 */
public class SubTest {

    public static void main(String[] args) throws Exception {

        ZKClientFactory zkClientFactory = new ZKClientFactory("localhost:2181");

        Subscriber subscriber = new Subscriber(zkClientFactory, "test-topic");

        subscriber.subscribe( it -> {
            String msg = new String(it);
            System.out.println(msg);
        });

        System.in.read();
    }
}
