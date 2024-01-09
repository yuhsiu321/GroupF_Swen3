package org.openapitools.configuration;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

@Slf4j
@Component
public class WebSocketServer implements InitializingBean {


    public void initWebSocketServer() {

        try {
            EventLoopGroup master = new NioEventLoopGroup();
            EventLoopGroup salve = new NioEventLoopGroup();

            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(master, salve);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.localAddress(new InetSocketAddress( 8000));
            bootstrap.childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();

                    //http编码器
                    pipeline.addLast(new HttpServerCodec());

                    pipeline.addLast(new HttpObjectAggregator(1024 * 10));

                    //此处设置映射路径
                    pipeline.addLast(getWebSocketServerProtocolHandler());

                    //自定义客户端处理器
                    pipeline.addLast(new WebSocketServerHandler());

                    //字符串编码器，用于发送应答消息
                    pipeline.addLast(new StringEncoder());
                }
            });


            ChannelFuture future = bootstrap.bind().sync();
            if (future.isSuccess()) {
                System.out.println("启动 ws server 成功");
            }
        } catch (InterruptedException e) {

        }
    }

    private WebSocketServerProtocolHandler getWebSocketServerProtocolHandler() {
        WebSocketServerProtocolConfig config = WebSocketServerProtocolConfig.newBuilder()
                .maxFramePayloadLength(1024 * 1024 * 10).websocketPath("/ws/status").build();
        return new WebSocketServerProtocolHandler(config);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initWebSocketServer();
    }
}