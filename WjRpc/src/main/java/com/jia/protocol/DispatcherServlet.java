package com.jia.protocol;
import com.jia.common.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DispatcherServlet extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 当接收到客户端的消息时被调用
        System.out.println("Server received: " + msg);

        // TODO: if name
        new NettyServerHandler().handler(ctx, (Invocation) msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 当出现异常时被调用
        cause.printStackTrace();
        ctx.close(); // 关闭该Channel
    }
}

