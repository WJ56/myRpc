package com.jia.protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Arrays;

public class NettyClient {
    public void send(String hostname, Integer port, byte[] serialize) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class) // 使用NIO进行网络传输
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new StringDecoder());
                            ch.pipeline().addLast(new StringEncoder());
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            // 连接到服务器
            ChannelFuture f = b.connect(hostname, port).sync();

            // 发送消息到服务器
            f.channel().writeAndFlush(serialize);

            // 当接收到客户端的消息时被调用
            System.out.println("Client sent: " + Arrays.toString(serialize));

            // 等待连接被关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

