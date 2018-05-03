package com.github.hykes.zk.pubsub;

/**
 * @author hehaiyangwork@gmail.com
 * @date 2018/5/3
 */
import com.google.common.io.Files;

public class Paths {

    private final String clientBase;
    private final String subscriberBase;
    private final String topicBase;

    public Paths(String basePath, String topic) {
        this.clientBase = Files.simplifyPath(basePath + "/clients");
        this.subscriberBase = basePath + "/subscribers/" + topic;
        this.topicBase = basePath + "/topics/" + topic;
    }

    public String getClientBase() {
        return this.clientBase;
    }

    public String getClientPathOfLocalhost() {
        return Files.simplifyPath(this.clientBase + "/" + HostNames.hostName);
    }

    public String getSubscriberBase() {
        return this.subscriberBase;
    }

    public String getSubscriberPathOfLocalhost() {
        return Files.simplifyPath(this.subscriberBase + "/" + HostNames.hostName);
    }

    public String getTopicBase() {
        return this.topicBase;
    }

    public String getTopicPathOfLocalHost() {
        return this.getTopicPathOfHost(HostNames.hostName);
    }

    public String getTopicPathOfHost(String hostName) {
        return Files.simplifyPath(this.topicBase + "/" + hostName);
    }
}
