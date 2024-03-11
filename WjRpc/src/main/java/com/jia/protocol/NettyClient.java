package com.jia.protocol;
import com.jia.common.Invocation;
import com.jia.common.KryoDecoder;
import com.jia.common.KryoEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Arrays;

public class NettyClient {
    public void send(String hostname, Integer port, Invocation invocation) throws Exception {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class) // 使用NIO进行网络传输
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        public void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new KryoDecoder()); // 解码器
                            ch.pipeline().addLast(new KryoEncoder()); // 编码器
                            ch.pipeline().addLast(new NettyClientHandler());
                        }
                    });

            // 连接到服务器
            ChannelFuture f = b.connect(hostname, port).sync();

            // 发送消息到服务器
            f.channel().writeAndFlush(invocation);

            // 当接收到客户端的消息时被调用
            System.out.println("Client sent: " + invocation);

            // 等待连接被关闭
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

