package org.openapitools.configuration;

import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 使用静态内部类来实现懒加载的单例模式，因为JVM在加载静态内部类时会保证线程安全
 */
public class ChannelMap {
    
    private static class ChannelMapHolder {
        // ChannelMap存储客户端的ID标识与通道的对应关系
        private static final ConcurrentHashMap<String, Channel> INSTANCE = new ConcurrentHashMap<>();
    }
    
    public static ConcurrentHashMap<String, Channel> getChannelMap() {
        return ChannelMapHolder.INSTANCE;
    }
    
    public static Channel getChannel(String id) {
        return getChannelMap().get(id);
    }
    
}
