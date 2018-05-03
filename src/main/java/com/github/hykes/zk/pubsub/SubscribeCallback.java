package com.github.hykes.zk.pubsub;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/5/3
 */
public interface SubscribeCallback {

    void run(byte[] bytes);
}