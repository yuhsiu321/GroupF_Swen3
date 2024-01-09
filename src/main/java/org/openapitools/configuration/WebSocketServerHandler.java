package org.openapitools.configuration;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object byteBuf) throws Exception {
        //websocket建立连接的时候，服务端把客户端的channelId存起来，客户端通过http上传文件的时候，把channelId传到服务器，
        // 服务器处理完上传逻辑之后发送websocket消息通知客户端更新进度
//        if (byteBuf instanceof TextWebSocketFrame) {
//            String text = ((TextWebSocketFrame) byteBuf).text();
//            ChannelMap.getChannelMap().put(text, channelHandlerContext.channel());
//        }
    }


    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ChannelMap.getChannelMap().put("channel", ctx.channel());
        System.out.println("新客户端建立连接...");

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端断开连接...");
    }
}

