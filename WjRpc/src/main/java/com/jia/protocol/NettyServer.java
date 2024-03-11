package com.jia.protocol;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

public class NettyServer {
    public void start(String hostname, Integer port) {
        // 创建两个NioEventLoopGroup，通常一个用于接收客户端的连接，另一个用于处理客户端的读写事件
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建ServerBootstrap实例，用于设置服务器的相关参数
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // 指定使用NIO的通信模式
                    .childHandler(new ChannelInitializer<Channel>() { // 设置ChannelInitializer，对新连接的Channel进行初始化
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new KryoCode.KryoDecoder()); // 解码器
                            ch.pipeline().addLast(new KryoCode.KryoEncoder()); // 编码器
                            ch.pipeline().addLast(new DispatcherServlet()); // 自定义的处理器
                        }
                    });

            // 绑定端口并启动服务器
            ChannelFuture f = b.bind(new InetSocketAddress(hostname, port)).sync();
            // 到这里没有异常，表示服务已经启动
            System.out.println("Server started successfully on port: " + port);

            // 等待服务器socket关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 优雅地关闭EventLoopGroup，释放资源
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}

